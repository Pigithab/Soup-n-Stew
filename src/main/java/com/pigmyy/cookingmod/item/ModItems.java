package com.pigmyy.cookingmod.item;

import com.pigmyy.cookingmod.CookingMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CookingMod.MOD_ID);


    public static final RegistryObject<Item> FISH_SOUP = ITEMS.register("fish_soup", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> VEGETABLE_SOUP = ITEMS.register("vegetable_soup", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ROTTEN_SOUP = ITEMS.register("rotten_soup", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PUMPKIN_SOUP = ITEMS.register("pumpkin_soup", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PORK_SOUP = ITEMS.register("pork_soup", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BEEF_SOUP = ITEMS.register("beef_soup", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHICKEN_SOUP = ITEMS.register("chicken_soup", () -> new Item(new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
