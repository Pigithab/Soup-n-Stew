package com.pigmyy.cookingmod.item;

import com.pigmyy.cookingmod.CookingMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CookingMod.MOD_ID);

    // Register every item ( and add them to .food() since its a food mod)
    public static final RegistryObject<Item> FISH_STEW = ITEMS.register("fish_stew", () -> new Item(new Item.Properties().food(ModFoodProperties.FISH_STEW)));

    public static final RegistryObject<Item> VEGETABLE_STEW = ITEMS.register("vegetable_stew", () -> new Item(new Item.Properties().food(ModFoodProperties.VEGETABLE_STEW)));

    public static final RegistryObject<Item> ROTTEN_STEW = ITEMS.register("rotten_stew", () -> new Item(new Item.Properties().food(ModFoodProperties.ROTTEN_STEW)));

    public static final RegistryObject<Item> PUMPKIN_SOUP = ITEMS.register("pumpkin_soup", () -> new Item(new Item.Properties().food(ModFoodProperties.PUMPKIN_SOUP)));

    public static final RegistryObject<Item> PORK_STEW = ITEMS.register("pork_stew", () -> new Item(new Item.Properties().food(ModFoodProperties.PORK_STEW)));

    public static final RegistryObject<Item> BEEF_STEW = ITEMS.register("beef_stew", () -> new Item(new Item.Properties().food(ModFoodProperties.BEEF_STEW)));

    public static final RegistryObject<Item> CHICKEN_STEW = ITEMS.register("chicken_stew", () -> new Item(new Item.Properties().food(ModFoodProperties.CHICKEN_STEW)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
