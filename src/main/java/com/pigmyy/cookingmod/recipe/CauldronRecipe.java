//package com.pigmyy.cookingmod.recipe;
//
//import net.minecraft.core.HolderLookup;
//import net.minecraft.core.NonNullList;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.Level;
//
//public record CauldronRecipe(Ingredient inputItem, ItemStack output) implements Recipe<CauldronRecipeInput> {
//    @Override
//    public NonNullList<Ingredient> getIngredients() {
//        NonNullList<Ingredient> list = NonNullList.create();
//        list.add(inputItem);
//        return list;
//    }
//
//
//    @Override
//    public boolean matches(CauldronRecipeInput pInput, Level pLevel) {
//        if(pLevel.isClientSide()) {
//            return false;
//        }
//
//        // if item in inventory == recipe return true
//        return inputItem.test(pInput.getItem(0));
//    }
//
//    @Override
//    public ItemStack assemble(CauldronRecipeInput pInput, HolderLookup.Provider pRegistries) {
//        return output.copy();
//    }
//
//    @Override
//    public boolean canCraftInDimensions(int pWidth, int pHeight) {
//        return false;
//    }
//
//    @Override
//    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
//        return output;
//    }
//
//    @Override
//    public RecipeSerializer<?> getSerializer() {
//        return
//    }
//
//    @Override
//    public RecipeType<?> getType() {
//        return
//    }
//
//    public static class
//}
