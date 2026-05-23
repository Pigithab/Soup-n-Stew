package com.pigmyy.cookingmod.item;

import com.pigmyy.cookingmod.CookingMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CookingMod.MOD_ID);

    // registering individual items
    public static final RegistryObject<Item> GH0STFISH = ITEMS.register("gh0stfish", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AlEXPECTED = ITEMS.register("alexpected", () -> new Item(new Item.Properties()));
    


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
