package com.indref.industrial_reforged.datagen.data;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IRRecipeBuilder<T extends Recipe<?>> implements RecipeBuilder {
    private final T recipe;

    private IRRecipeBuilder(T recipe) {
        this.recipe = recipe;
    }

    public static <T extends Recipe<?>> IRRecipeBuilder<T> of(T recipe) {
        return new IRRecipeBuilder<>(recipe);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        ItemStack itemStack = recipe.getResultItem(null);
        return itemStack.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (Ingredient.Value value : ingredient.getValues()) {
                if (value instanceof Ingredient.ItemValue(ItemStack item)) {
                    ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item.getItem());
                    builder.append(itemLocation.getPath()).append("_");
                } else if (value instanceof Ingredient.TagValue(TagKey<Item> tag)) {
                    builder.append(tag.location().getPath()).append("_");
                }
            }
        }
        Item result = getResult();
        if (result != Items.AIR) {
            builder.append("to_").append(BuiltInRegistries.ITEM.getKey(result).getPath());
        } else {
            builder.deleteCharAt(builder.length() - 1);
        }
        save(recipeOutput, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, recipe.getType() + "/" + builder));
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        recipeOutput.accept(id, recipe, null);
    }
}
