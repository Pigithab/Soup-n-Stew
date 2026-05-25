package com.pigmyy.cookingmod.block.custom;

import com.mojang.serialization.MapCodec;
import com.pigmyy.cookingmod.block.entity.ModBlockEntities;
import com.pigmyy.cookingmod.block.entity.custom.CauldronBlockEntity;
import com.pigmyy.cookingmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Nullable;


public class Cauldron extends BaseEntityBlock {


    // Default Cauldron Shape Copied from 'AbstractCauldronBlock.java'
    private static final VoxelShape INSIDE = box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
    protected static final VoxelShape SHAPE = Shapes.join(
            Shapes.block(),
            Shapes.or(box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0), box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), INSIDE),
            BooleanOp.ONLY_FIRST);
    public static final MapCodec<Cauldron> CODEC = simpleCodec(Cauldron::new);


    public Cauldron(Properties pProperties) {
        super(pProperties);
        //DEFAULT STATES

        // Cauldron Integers for Water Level and Soup Type
        this.registerDefaultState(this.stateDefinition.any()
                // LEVEL INTEGER
                .setValue(LEVEL, 0)
                // SOUP_TYPE ENUM
                .setValue(SOUPTYPE, SoupType.WATER));


    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState){
        return new CauldronBlockEntity(pPos, pState);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
                if(pState.getBlock() != pNewState.getBlock()) {
                    if(pLevel.getBlockEntity(pPos) instanceof CauldronBlockEntity CauldronBlockEntity) {
                        CauldronBlockEntity.drops();
                        pLevel.updateNeighbourForOutputSignal(pPos, this);

                    }
                }
                super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }


    // Integer Water "LEVEL"
    public static final net.minecraft.world.level.block.state.properties.IntegerProperty LEVEL =
            net.minecraft.world.level.block.state.properties.IntegerProperty.create("level", 0, 3);
    // Enum "SOUPTYPE"
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<SoupType> SOUPTYPE =
            net.minecraft.world.level.block.state.properties.EnumProperty.create("soup_type", SoupType.class);

    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, SOUPTYPE);
    }

    // HELPER TO CHANGE SOUP TYPE FROM OUTSIDE
    public static void changeSoupState(Level pLevel, BlockPos pPos, SoupType type) {
        BlockState pState = pLevel.getBlockState(pPos);
        pLevel.setBlockAndUpdate(pPos, pState.setValue(SOUPTYPE, type));
    }





    // INTERACTION HANDLER
    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {

        //ADD WATER
        if (pStack.is(Items.WATER_BUCKET) && (pState.getValue(LEVEL) < 3 || pState.getValue(SOUPTYPE) != SoupType.WATER)) {
            if (!pLevel.isClientSide()) {
                // Set LEVEL and SOUPTYPE
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LEVEL, 3).setValue(SOUPTYPE, SoupType.WATER));

                // Switch the Water Bucket in player hand to Empty Bucket
                ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                pPlayer.setItemInHand(pHand, net.minecraft.world.item.ItemUtils.createFilledResult(pStack, pPlayer, emptyBucket));

                // Sound
                pLevel.playSound(null, pPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        // REMOVE WATER
        } else if (pStack.is(Items.BUCKET) && pState.getValue(LEVEL) == 3) {
            if (!pLevel.isClientSide()) {
                // Reset LEVEL and SOUPTYPE
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LEVEL, 0).setValue(SOUPTYPE, SoupType.WATER));

                // Switch the Empty Bucket in player hand to Water Bucket
                ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);
                pPlayer.setItemInHand(pHand, net.minecraft.world.item.ItemUtils.createFilledResult(pStack, pPlayer, waterBucket));

                // Sound
                pLevel.playSound(null, pPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        // BOTTLE
        }else if (pStack.is(Items.GLASS_BOTTLE) && pState.getValue(LEVEL) > 0 && pState.getValue(SOUPTYPE) == SoupType.WATER) {
            if (!pLevel.isClientSide()) {
                // Reset LEVEL and SOUPTYPE
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LEVEL, (pState.getValue(LEVEL) - 1)));

                // Switch the Empty Bottle in player hand to Water Bottle
                ItemStack waterBottle = net.minecraft.world.item.alchemy.PotionContents.createItemStack(Items.POTION, net.minecraft.world.item.alchemy.Potions.WATER);
                pPlayer.setItemInHand(pHand, net.minecraft.world.item.ItemUtils.createFilledResult(pStack, pPlayer, waterBottle));

                // Sound
                pLevel.playSound(null, pPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

        // STEW
        }else if (pStack.is(Items.BOWL) && pState.getValue(LEVEL) > 0 && pState.getValue(SOUPTYPE) != SoupType.WATER) {
            // Reset LEVEL and SOUPTYPE
            pLevel.setBlockAndUpdate(pPos, pState.setValue(LEVEL, (pState.getValue(LEVEL) - 1)));

            // Switch the Bowl in player hand to stew according the Cauldrons Stew Type
            ItemStack stew = switch (pState.getValue(SOUPTYPE)) {
                case MUSHROOM  -> new ItemStack(Items.MUSHROOM_STEW);
                case RABBIT    -> new ItemStack(Items.RABBIT_STEW);
                case BEETROOT  -> new ItemStack(Items.BEETROOT_SOUP);
                case FISH      -> new ItemStack(ModItems.FISH_STEW.get());
                case PORK      -> new ItemStack(ModItems.PORK_STEW.get());
                case BEEF      -> new ItemStack(ModItems.BEEF_STEW.get());
                case ROTTEN    -> new ItemStack(ModItems.ROTTEN_STEW.get());
                case CHICKEN   -> new ItemStack(ModItems.CHICKEN_STEW.get());
                case PUMPKIN   -> new ItemStack(ModItems.PUMPKIN_SOUP.get());
                case VEGETABLE -> new ItemStack(ModItems.VEGETABLE_STEW.get());
                default -> new ItemStack(Items.BOWL);
            };
            pPlayer.setItemInHand(pHand, net.minecraft.world.item.ItemUtils.createFilledResult(pStack, pPlayer, stew));


            // Sound
            pLevel.playSound(null, pPos, SoundEvents.FROG_EAT, SoundSource.BLOCKS, 2.0F, 1.0F);


        // OPEN MENU
        }else if (pLevel.getBlockEntity(pPos) instanceof CauldronBlockEntity cauldronBlockEntity) {
            if((!pPlayer.isCrouching() && !pLevel.isClientSide) || (pPlayer.isCrouching() && !pLevel.isClientSide && pStack.isEmpty())){
                ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(cauldronBlockEntity, Component.literal("Cauldron")), pPos);
                return ItemInteractionResult.SUCCESS;
            }

            if(pPlayer.isCrouching() && pLevel.isClientSide()) {
                return ItemInteractionResult.SUCCESS;
            }
        }



        return ItemInteractionResult.SUCCESS;
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.CAULDON_BE.get(),
                (level, blockPos, blockState, CauldronBlockEntity) -> CauldronBlockEntity.tick(level, blockPos, blockState));
    }
}

