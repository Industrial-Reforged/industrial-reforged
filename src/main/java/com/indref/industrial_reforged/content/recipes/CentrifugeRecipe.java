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

import java.util.List;

public record CentrifugeRecipe(IngredientWithCount ingredient, List<ItemStack> results, FluidStack resultFluid,
                               int duration, int energy) implements PDLRecipe<SingleRecipeInput> {
    public static final String NAME = "centrifuge";
    public static final RecipeType<CentrifugeRecipe> TYPE = RecipeUtils.newRecipeType(NAME);
    public static final RecipeSerializer<CentrifugeRecipe> SERIALIZER =
            RecipeUtils.newRecipeSerializer(IRRecipeSerializer.Centrifuge.CODEC, IRRecipeSerializer.Centrifuge.STREAM_CODEC);

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level) {
        return ingredient.test(recipeInput.item());
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, ingredient.ingredient());
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return results.getFirst();
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
