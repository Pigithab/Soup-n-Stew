package com.pigmyy.cookingmod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pigmyy.cookingmod.recipe.CauldronRecipeInput;
import com.pigmyy.cookingmod.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import java.util.Optional;

// inputItem2 & 3 are defined as optional since recipes dont always have 3 ingredients
public record CauldronRecipe(Ingredient inputItem1, Optional<Ingredient> inputItem2, Optional<Ingredient> inputItem3, ItemStack output) implements Recipe<CauldronRecipeInput> {
    @Override
    public NonNullList<Ingredient> getIngredients() {
        // create the list of the ingredients, depending on the amount
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem1);
        inputItem2.ifPresent(list::add);
        inputItem3.ifPresent(list::add);
        return list;
    }
    // Checks if items inside the cauldron match the recipe
    @Override
    public boolean matches(CauldronRecipeInput pInput, Level pLevel) {

        if(pLevel.isClientSide()) { return false; }
        // counts how many slots aren't empty
        int totalNonEmptySlots = 0;
        for (ItemStack stack : pInput.inputs()) {
            if (!stack.isEmpty()) {
                totalNonEmptySlots++;
            }
        }
        // counts how many ingredients the recipe needs
        int requiredIngredientsCount = 1;
        if (inputItem2.isPresent()) requiredIngredientsCount++;
        if (inputItem3.isPresent()) requiredIngredientsCount++;

        // compares if the recipe and the items have the same count
        if (totalNonEmptySlots != requiredIngredientsCount) {
            return false;
        }


        boolean match1 = false;
        boolean match2 = !inputItem2.isPresent();
        boolean match3 = !inputItem3.isPresent();
        // check each ingredient individually regardless of order
        for (ItemStack stack : pInput.inputs()) {
            if (!stack.isEmpty()) {
                if (!match1 && inputItem1.test(stack)) {
                    match1 = true;
                } else if (!match2 && inputItem2.get().test(stack)) {
                    match2 = true;
                } else if (!match3 && inputItem3.get().test(stack)) {
                    match3 = true;
                }
            }
        }

        return match1 && match2 && match3;
    }

    @Override
    public ItemStack assemble(CauldronRecipeInput pInput, HolderLookup.Provider pRegistries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CAULDRON_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CAULDRON_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CauldronRecipe> {

        // define how to read the json file to get the recipe
        public static final MapCodec<CauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient1").forGetter(CauldronRecipe::inputItem1),
                Ingredient.CODEC_NONEMPTY.optionalFieldOf("ingredient2").forGetter(CauldronRecipe::inputItem2),
                Ingredient.CODEC_NONEMPTY.optionalFieldOf("ingredient3").forGetter(CauldronRecipe::inputItem3),
                ItemStack.CODEC.fieldOf("result").forGetter(CauldronRecipe::output)
        ).apply(inst, CauldronRecipe::new));

        // defines how to recipe data between server and client
        public static final StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, CauldronRecipe::inputItem1,
                        ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), CauldronRecipe::inputItem2,
                        ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), CauldronRecipe::inputItem3,
                        ItemStack.STREAM_CODEC, CauldronRecipe::output,
                        CauldronRecipe::new);

        @Override
        public MapCodec<CauldronRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
