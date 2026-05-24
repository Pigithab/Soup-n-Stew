package com.pigmyy.cookingmod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
// ACTUALLY MAKES THE ITEMS EDIBLE
public class ModFoodProperties {
    public static final FoodProperties FISH_SOUP = soup(4, 0.65F).build();
    public static final FoodProperties BEEF_SOUP = soup(5, 0.8F).build();
    public static final FoodProperties PORK_SOUP = soup(6, 0.6F).build();
    public static final FoodProperties PUMPKIN_SOUP = soup(8, 0.8F).build();
    public static final FoodProperties CHICKEN_SOUP = soup(7, 0.6F).build();
    public static final FoodProperties VEGETABLE_SOUP = soup(6, 0.6F).build();
    public static final FoodProperties ROTTEN_SOUP = soup(5, 0.2F)
            .effect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.7F).build();

    private static FoodProperties.Builder soup(int pNutrition, float pSaturation) {
        return new FoodProperties.Builder().nutrition(pNutrition).saturationModifier(pSaturation).usingConvertsTo(Items.BOWL);
    }
}
