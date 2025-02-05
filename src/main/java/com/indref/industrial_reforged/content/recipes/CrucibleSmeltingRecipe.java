package com.indref.industrial_reforged.content.recipes;

import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import com.portingdeadmods.portingdeadlibs.api.recipes.PDLRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record CrucibleSmeltingRecipe(IngredientWithCount ingredient, FluidStack resultFluid, int duration,
                                     int heat) implements PDLRecipe<SingleRecipeInput> {
    public static final String NAME = "crucible_melting";
    public static final RecipeType<CrucibleSmeltingRecipe> TYPE = RecipeUtils.newRecipeType(NAME);
    public static final RecipeSerializer<CrucibleSmeltingRecipe> SERIALIZER =
            RecipeUtils.newRecipeSerializer(IRRecipeSerializer.CrucibleMelting.CODEC, IRRecipeSerializer.CrucibleMelting.STREAM_CODEC);

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level) {
        return ingredient.test(recipeInput.item());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SingleRecipeInput recipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, RecipeUtils.iWCToIngredientSaveCount(ingredient));
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    public FluidStack resultFluid() {
        return resultFluid.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TYPE;
    }
}
