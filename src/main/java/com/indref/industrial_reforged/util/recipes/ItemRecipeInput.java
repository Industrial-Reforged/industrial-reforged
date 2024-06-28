package com.indref.industrial_reforged.util.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record ItemRecipeInput(List<ItemStack> items) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return items.get(i);
    }

    @Override
    public int size() {
        return items.size();
    }
}
