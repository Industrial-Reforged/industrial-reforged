package com.indref.industrial_reforged.content.recipes;

import com.indref.industrial_reforged.api.recipes.IRRecipe;
import com.indref.industrial_reforged.util.recipes.*;
import com.indref.industrial_reforged.util.Utils;
import com.indref.industrial_reforged.util.recipes.recipeInputs.ItemRecipeInput;
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

public record BlastFurnaceRecipe(NonNullList<IngredientWithCount> ingredients, FluidStack resultFluid,
                                 int duration) implements IRRecipe<ItemRecipeInput> {
    public static final String NAME = "blast_furnace";
    public static final RecipeType<BlastFurnaceRecipe> TYPE = RecipeUtils.newRecipeType(NAME);
    public static final RecipeSerializer<BlastFurnaceRecipe> SERIALIZER =
            RecipeUtils.newRecipeSerializer(IRRecipeSerializer.BlastFurnace.CODEC, IRRecipeSerializer.BlastFurnace.STREAM_CODEC);

    public BlastFurnaceRecipe(List<IngredientWithCount> ingredients, FluidStack resultFluid, int duration) {
        this(Utils.listToNonNullList(ingredients), resultFluid, duration);
    }

    @Override
    public boolean matches(ItemRecipeInput recipeInput, Level level) {
        return RecipeUtils.compareItems(recipeInput.items(), ingredients);
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return Utils.listToNonNullList(RecipeUtils.iWCToIngredientsSaveCount(ingredients));
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TYPE;
    }

    public FluidStack resultFluid() {
        return resultFluid.copy();
    }
}
