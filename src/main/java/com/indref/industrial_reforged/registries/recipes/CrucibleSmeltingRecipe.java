package com.indref.industrial_reforged.registries.recipes;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.recipes.IRRecipe;
import com.indref.industrial_reforged.util.recipes.recipe_inputs.ItemRecipeInput;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CrucibleSmeltingRecipe(Ingredient ingredient, FluidStack resultFluid, int duration,
                                     int heat) implements IRRecipe<ItemRecipeInput> {
    public static final String NAME = "crucible_melting";
    public static final RecipeType<CrucibleSmeltingRecipe> TYPE = RecipeUtils.newRecipeType(NAME);
    public static final RecipeSerializer<CrucibleSmeltingRecipe> SERIALIZER =
            RecipeUtils.newRecipeSerializer(IRRecipeSerializer.CrucibleMelting.CODEC, IRRecipeSerializer.CrucibleMelting.STREAM_CODEC);

    public CrucibleSmeltingRecipe(List<Ingredient> input, FluidStack output, int duration, int heat) {
        this(input.get(0), output, duration, heat);
    }

    @Override
    public boolean matches(ItemRecipeInput recipeInput, Level level) {
        IndustrialReforged.LOGGER.debug("in: {}, existing: {}", recipeInput.getItem(0), ingredient);
        return ingredient.test(recipeInput.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ItemRecipeInput recipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, ingredient);
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
