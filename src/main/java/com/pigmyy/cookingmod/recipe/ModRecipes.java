package com.pigmyy.cookingmod.recipe;

import com.pigmyy.cookingmod.CookingMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CookingMod.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CookingMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CauldronRecipe>> CAULDRON_SERIALIZER = SERIALIZERS.register("cauldron", CauldronRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<CauldronRecipe>> CAULDRON_TYPE = TYPES.register("cauldron", () -> new RecipeType<CauldronRecipe>() {
        @Override
        public String toString() {
            return "cauldron";
        }
    });

    public static void register(IEventBus eventBus) {
        System.out.println("Registering recipes");
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
