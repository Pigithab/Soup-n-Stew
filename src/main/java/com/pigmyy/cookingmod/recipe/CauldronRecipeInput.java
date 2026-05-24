package com.pigmyy.cookingmod.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import java.util.List;

public record CauldronRecipeInput(List<ItemStack> inputs) implements RecipeInput {
    @Override
    public ItemStack getItem(int pIndex) {
        return inputs.get(pIndex);
    }

    @Override
    public int size() {
        return inputs.size();
    }
}
