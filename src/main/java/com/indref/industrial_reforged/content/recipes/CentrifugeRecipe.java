package com.indref.industrial_reforged.registries.recipes;

import com.indref.industrial_reforged.api.recipes.IRRecipe;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.recipeInputs.ItemRecipeInput;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CentrifugeRecipe(IngredientWithCount ingredient, List<ItemStack> results,
                               int duration, int energy) implements IRRecipe<ItemRecipeInput> {
    public static final String NAME = "centrifuge";
    public static final RecipeType<CentrifugeRecipe> TYPE = RecipeUtils.newRecipeType(NAME);
    public static final RecipeSerializer<CentrifugeRecipe> SERIALIZER =
            RecipeUtils.newRecipeSerializer(IRRecipeSerializer.Centrifuge.CODEC, IRRecipeSerializer.Centrifuge.STREAM_CODEC);

    @Override
    public boolean matches(ItemRecipeInput recipeInput, Level level) {
        return ingredient.test(recipeInput.getItem(0));
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
}
