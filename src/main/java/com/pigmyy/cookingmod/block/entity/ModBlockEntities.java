package com.pigmyy.cookingmod.block.entity;

import com.pigmyy.cookingmod.CookingMod;
import com.pigmyy.cookingmod.block.ModBlocks;
import com.pigmyy.cookingmod.block.entity.custom.CauldronBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CookingMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<CauldronBlockEntity>> CAULDON_BE = BLOCK_ENTITIES.register("cauldon_be", () ->
    BlockEntityType.Builder.of(CauldronBlockEntity::new, ModBlocks.CAULDRON.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
