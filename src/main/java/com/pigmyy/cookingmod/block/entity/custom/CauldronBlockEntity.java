package com.pigmyy.cookingmod.block.entity.custom;

import com.pigmyy.cookingmod.block.custom.Cauldron;
import com.pigmyy.cookingmod.block.custom.SoupType;
import com.pigmyy.cookingmod.block.entity.ModBlockEntities;
import com.pigmyy.cookingmod.item.ModItems;
import com.pigmyy.cookingmod.screen.custom.CauldronMenu;
import com.pigmyy.cookingmod.screen.custom.CauldronScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CauldronBlockEntity extends BlockEntity implements MenuProvider {

    // INVENTORY
    public final ItemStackHandler inventory = new ItemStackHandler(4) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
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
    private int maxFuel = 4000;


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
        // EVERY TICK EXECUTE FOLLOWING CODE
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        addFuel();
        if(hasRecipe() && hasWater() && hasFuel()) { // Check if Recipe is Correct and if the Cauldron has water
            increaseCookingProgress(); // Increases CookingProgress by 1
            setChanged(level, blockPos, blockState);
            // when cooking is finished actually do the cooking and reset progress
            if(hasCookingFinished()) {
                cookSoup();
                resetProgress();
            }


        } else {
            // if one of the requirements fails reset progress
            resetProgress();
        }
    }

    private boolean hasFuel() {
        return this.fuelLeft > 0;
    }

    private void addFuel() {
        int itemBurnDuration = net.minecraftforge.common.ForgeHooks.getBurnTime(inventory.getStackInSlot(FUEL_SLOT), RecipeType.SMELTING);
        if (itemBurnDuration != 0 && itemBurnDuration + fuelLeft < maxFuel) {
            fuelLeft += itemBurnDuration;
            inventory.extractItem(FUEL_SLOT, 1, false);
        }
        return;
    }

    private boolean hasWater() {
        BlockState state = this.level.getBlockState(this.worldPosition);
        return (state.getValue(Cauldron.LEVEL) > 0 && state.getValue(Cauldron.SOUPTYPE) == SoupType.WATER);
    }

    private void resetProgress() {
        this.progress = 0;
        maxProgress = 400;
    }

    private void cookSoup() {
        inventory.extractItem(INPUT_SLOT1, 1, false);
        com.pigmyy.cookingmod.block.custom.Cauldron.changeSoupState(this.level, this.worldPosition, SoupType.CARROT);
    }

    private boolean hasCookingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCookingProgress() {
        progress++;
        fuelLeft--;
    }

    private boolean hasRecipe() {
        Item input = Items.CARROT;

        return inventory.getStackInSlot(INPUT_SLOT1).is(input) || inventory.getStackInSlot(INPUT_SLOT2).is(input) || inventory.getStackInSlot(INPUT_SLOT3).is(input);
    }
}
