package com.pigmyy.cookingmod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
// Makes the registered items edible while also assigning them nutrition and saturation values + bonus effects
public class ModFoodProperties {
    public static final FoodProperties FISH_STEW = soup(4, 0.65F).build();
    public static final FoodProperties BEEF_STEW = soup(5, 0.8F).build();
    public static final FoodProperties PORK_STEW = soup(6, 0.6F).build();
    public static final FoodProperties PUMPKIN_SOUP = soup(8, 0.8F).build();
    public static final FoodProperties CHICKEN_STEW = soup(7, 0.6F).build();
    public static final FoodProperties VEGETABLE_STEW = soup(6, 0.6F).build();
    public static final FoodProperties ROTTEN_STEW = soup(5, 0.2F)
            .effect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.7F).build();

    // function to save time since it's used for literally every item ( same as what minecraft normally does for stews )
    private static FoodProperties.Builder soup(int pNutrition, float pSaturation) {
        return new FoodProperties.Builder().nutrition(pNutrition).saturationModifier(pSaturation).usingConvertsTo(Items.BOWL);
    }
}
