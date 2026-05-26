package com.pigmyy.cookingmod.block.entity.custom;

import com.pigmyy.cookingmod.block.custom.Cauldron;
import com.pigmyy.cookingmod.block.custom.SoupType;
import com.pigmyy.cookingmod.block.entity.ModBlockEntities;
import com.pigmyy.cookingmod.item.ModItems;
import com.pigmyy.cookingmod.recipe.CauldronRecipe;
import com.pigmyy.cookingmod.recipe.CauldronRecipeInput;
import com.pigmyy.cookingmod.recipe.ModRecipes;
import com.pigmyy.cookingmod.screen.custom.CauldronMenu;
import com.pigmyy.cookingmod.screen.custom.CauldronScreen;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static com.pigmyy.cookingmod.block.custom.Cauldron.LEVEL;

public class CauldronBlockEntity extends BlockEntity implements MenuProvider {

    // INVENTORY
    public final ItemStackHandler inventory = new ItemStackHandler(4) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            if (slot == 3) {return stack.getMaxStackSize(); }
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };


    // initialize variables
    private static final int INPUT_SLOT1 = 0;
    private static final int INPUT_SLOT2 = 1;
    private static final int INPUT_SLOT3 = 2;
    private static final int FUEL_SLOT = 3;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    // Progress to track the cooking process + animated progress bar
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 400;
    private int fuelLeft = 0;
    private int maxFuel = 20000;
    private int fuelTimer = 0;
    private String cookingRecipe = "";
    public boolean isCooking = false;


    public CauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CAULDON_BE.get(), pPos, pBlockState);
        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> CauldronBlockEntity.this.progress;
                    case 1 -> CauldronBlockEntity.this.maxProgress;
                    case 2 -> CauldronBlockEntity.this.fuelLeft;
                    case 3 -> CauldronBlockEntity.this.maxFuel;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> CauldronBlockEntity.this.progress = pValue;
                    case 1 -> CauldronBlockEntity.this.maxProgress = pValue;
                    case 2 -> CauldronBlockEntity.this.fuelLeft = pValue;
                    case 3 -> CauldronBlockEntity.this.maxFuel = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> inventory);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void drops(){
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }


    // SAVE
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inventory", inventory.serializeNBT(pRegistries));
        pTag.putInt("cauldron.progress", progress);
        pTag.putInt("cauldron.max_progress", maxProgress);
        pTag.putInt("cauldron.fuel_left", fuelLeft);
        pTag.putInt("cauldron.max_fuel", maxFuel);
        pTag.putInt("cauldron.fuel_timer", fuelTimer);
        pTag.putString("cauldron.cookingRecipe", cookingRecipe);
        pTag.putBoolean("cauldron.isCooking", isCooking);
    }
    // LOAD
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        inventory.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("cauldron.progress");
        maxProgress = pTag.getInt("cauldron.max_progress");
        fuelLeft = pTag.getInt("cauldron.fuel_left");
        maxFuel = pTag.getInt("cauldron.max_fuel");
        fuelTimer = pTag.getInt("cauldron.fuel_timer");
        cookingRecipe = pTag.getString("cauldron.cookingRecipe");
        isCooking = pTag.getBoolean("cauldron.isCooking");
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        tag.put("inventory", inventory.serializeNBT(pRegistries)); // Forces clean sync
        return tag;
    }


    @Override
    public Component getDisplayName() {
        return Component.literal("Cauldron");
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CauldronMenu(pContainerId, pPlayerInventory, this, this.data);
    }


        // EVERY TICK EXECUTE FOLLOWING CODE ( COOKING PROCESS )


    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        boolean sameRecipe = sameRecipe();
        addFuel();
        if(hasRecipe() && hasWater() && hasFuel() && sameRecipe) { // Check if Recipe is Correct and if the Cauldron has water and fuel
            increaseCookingProgress(); // Increases CookingProgress by 1
            playAnimation();
            setChanged(level, blockPos, blockState);

            // when cooking is finished actually do the cooking and reset progress
            if(hasCookingFinished()) {
                cookStew();
                resetProgress();
            }


        } else {
            if(hasRecipe() && hasWater() && sameRecipe) { decreaseCookingProgress(); } // Fuel is the problem then just start removing progress
            else { resetProgress(); } // If Water or Recipe is the problem then start over
        }
    }


    // END OF COOKING PROCESS



    private boolean sameRecipe() {
        Optional<RecipeHolder<CauldronRecipe>> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            Item currentItem = recipe.get().value().output().getItem();
            String currentRecipe = BuiltInRegistries.ITEM.getKey(currentItem).toString();

            if (!cookingRecipe.equals(currentRecipe)) {
                cookingRecipe = currentRecipe;
                // reset progress here because it triggers decreaseCookingProgress since its changed before the second check
                resetProgress();
                return false;
            }
        }
        return true;
    }


    private void decreaseCookingProgress() {
        if (progress > 0) { progress--; }
    }

    private boolean hasFuel() {
        return this.fuelLeft > 0;
    }

    private void addFuel() {
        int itemBurnDuration = net.minecraftforge.common.ForgeHooks.getBurnTime(inventory.getStackInSlot(FUEL_SLOT), RecipeType.SMELTING);
        // if the item can give fuel && its fuel can fit inside the maxFuel
        if (fuelTimer > 0) { fuelTimer --; }
        else if (itemBurnDuration != 0 && itemBurnDuration + fuelLeft < maxFuel) {
            fuelLeft += itemBurnDuration;
            inventory.extractItem(FUEL_SLOT, 1, false);
            fuelTimer = 20;
        }
        return;
    }

    private boolean hasWater() {
        BlockState state = this.level.getBlockState(this.worldPosition);
        return (state.getValue(LEVEL) > 0 && state.getValue(Cauldron.SOUPTYPE) == SoupType.WATER);
    }

    private void resetProgress() {
        // max progress reset just in case some recipes take longer ( not a feature yet )
        this.progress = 0;
        maxProgress = 400;
        isCooking = false;
    }

    private void cookStew() {
        Optional<RecipeHolder<CauldronRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().output();
        SoupType soup = SoupType.WATER;
        // I AM SORRY YOU CANT MAKE A SWITCH IT ONLY ACCEPTS PRIMITIVES
        if (output.is(Items.MUSHROOM_STEW)) { soup = SoupType.MUSHROOM; }
        else if (output.is(Items.RABBIT_STEW)) { soup = SoupType.RABBIT; }
        else if (output.is(Items.BEETROOT_SOUP)) { soup = SoupType.BEETROOT; }
        else if (output.is(ModItems.FISH_STEW.get())) { soup = SoupType.FISH; }
        else if (output.is(ModItems.BEEF_STEW.get())) { soup = SoupType.BEEF; }
        else if (output.is(ModItems.PORK_STEW.get())) { soup = SoupType.PORK; }
        else if (output.is(ModItems.ROTTEN_STEW.get())) { soup = SoupType.ROTTEN; }
        else if (output.is(ModItems.PUMPKIN_SOUP.get())) { soup = SoupType.PUMPKIN; }
        else if (output.is(ModItems.CHICKEN_STEW.get())) { soup = SoupType.CHICKEN; }
        else if (output.is(ModItems.VEGETABLE_STEW.get())) { soup = SoupType.VEGETABLE; }
        // Remove items from the ingredient slots

        extractItem(INPUT_SLOT1);
        extractItem(INPUT_SLOT2);
        extractItem(INPUT_SLOT3);
        com.pigmyy.cookingmod.block.custom.Cauldron.changeSoupState(this.level, this.worldPosition, soup);
    }

    private void extractItem(int slot) {
        if (inventory.getStackInSlot(slot).is(Items.HONEY_BOTTLE)) {
            inventory.setStackInSlot(slot, new ItemStack(Items.GLASS_BOTTLE));
        } else { inventory.extractItem(slot, 1, false); }
    }


    private boolean hasCookingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCookingProgress() {
        progress++;
        fuelLeft--;
        isCooking = true;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<CauldronRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }
        return true;
    }

    private Optional<RecipeHolder<CauldronRecipe>> getCurrentRecipe() {
        List<ItemStack> itemStacks = List.of(
                inventory.getStackInSlot(0),
                inventory.getStackInSlot(1),
                inventory.getStackInSlot(2)
        );
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.CAULDRON_TYPE.get(), new CauldronRecipeInput(itemStacks), this.level);

    }

    private void playAnimation() {

        RandomSource pRandom = this.level.getRandom();
        // Campfire sound ( same logic as the campfire )
        this.level.playSound(null, this.worldPosition,
                SoundEvents.CAMPFIRE_CRACKLE,
                SoundSource.BLOCKS,
                0.2F + pRandom.nextFloat(),
                pRandom.nextFloat() * 0.7F + 0.6F);

        if (this.level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            // Water splash particles
            serverLevel.sendParticles(
                    ParticleTypes.SPLASH,
                    (double)this.worldPosition.getX() + 0.5,
                    (double)this.worldPosition.getY() + 0.15 + (this.getBlockState().getValue(LEVEL) * 0.25D),
                    (double)this.worldPosition.getZ() + 0.5,
                    0,
                    0.0D,
                    0.1D,
                    0.0D,
                    9.0D
            );

            // 1 in 3 Chance to trigger smoke
            if (pRandom.nextInt() % 3 == 0) {
                // Campfire's normal logic
                serverLevel.sendParticles(
                        ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                        (double)this.worldPosition.getX() + 0.5 + pRandom.nextDouble() / 3.0 * (double)(pRandom.nextBoolean() ? 1 : -1),
                        (double)this.worldPosition.getY() + 0.15 + (this.getBlockState().getValue(LEVEL) * 0.25D) + pRandom.nextDouble(),
                        (double)this.worldPosition.getZ() + 0.5 + pRandom.nextDouble() / 3.0 * (double)(pRandom.nextBoolean() ? 1 : -1),
                        0,
                        0.0D,
                        0.05D,
                        0.0D,
                        1.0D
                );
            }


        }
    }

    public void damageLivingEntity(net.minecraft.world.entity.Entity pEntity, Level pLevel) {
        if (pEntity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {
            livingEntity.hurt(pLevel.damageSources().hotFloor(), 1.0F);
        }
    }
}
