package com.indref.industrial_reforged.util.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public final class IngredientUtils {
    public static List<IngredientWithCount> ingredientsToIWC(List<Ingredient> ingredients) {
        return ingredients.stream().map(ingredient -> new IngredientWithCount(ingredient, 1)).toList();
    }

    public static List<Ingredient> iWCToIngredients(List<IngredientWithCount> ingredientsWithCount) {
        return ingredientsWithCount.stream().map(IngredientWithCount::ingredient).toList();
    }

    public static List<Ingredient> iWCToIngredientsSaveCount(List<IngredientWithCount> ingredientsWithCount) {
        return ingredientsWithCount.stream().map(ingredientWithCount -> {
            Ingredient ingredient = ingredientWithCount.ingredient();
            for (ItemStack itemStack : ingredient.getItems()) {
                itemStack.setCount(ingredientWithCount.count());
            }
            return ingredient;
        }).toList();
    }
}
