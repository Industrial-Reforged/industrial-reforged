package com.indref.industrial_reforged.content.recipes;

import com.indref.industrial_reforged.api.recipes.IRRecipe;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public final class MoldCraftingRecipe extends CustomRecipe {
    public MoldCraftingRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasMold = false;
        boolean hasValidIngredient = false;
        outer:
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(IRItems.CLAY_MOLD_BLANK)) {
                hasMold = true;
            } else if (!stack.isEmpty()) {
                Map<ResourceKey<Item>, TagKey<Item>> dataMap = BuiltInRegistries.ITEM.getDataMap(IRDataMaps.MOLD_INGREDIENTS);
                for (Map.Entry<ResourceKey<Item>, TagKey<Item>> entry : dataMap.entrySet()) {
                    // A mold ingredient is in the grid
                    if (stack.is(entry.getValue()) && !hasValidIngredient) {
                        hasValidIngredient = true;
                        continue outer;
                    }
                }

                // Some other item is in the grid
                return false;
            }
        }
        return hasMold && hasValidIngredient;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                Map<ResourceKey<Item>, TagKey<Item>> dataMap = BuiltInRegistries.ITEM.getDataMap(IRDataMaps.MOLD_INGREDIENTS);
                for (Map.Entry<ResourceKey<Item>, TagKey<Item>> entry : dataMap.entrySet()) {
                    // A mold ingredient is in the grid
                    if (stack.is(entry.getValue())) {
                        return registries.holderOrThrow(entry.getKey()).value().getDefaultInstance();
                    }
                }

                // Some other item is in the grid
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return IRRecipes.MOLD_CRAFTING.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); i++) {
            ItemStack item = input.getItem(i);
            if (!item.is(IRItems.CLAY_MOLD_BLANK)) {
                nonnulllist.set(i, item.copy());
            }
        }

        return nonnulllist;
    }
}
