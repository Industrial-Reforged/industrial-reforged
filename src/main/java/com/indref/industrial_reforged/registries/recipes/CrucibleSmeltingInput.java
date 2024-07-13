package com.indref.industrial_reforged.registries.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public record CrucibleSmeltingInput(ItemStack toSmelt, int heat) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int i) {
        return i == 0 ? toSmelt : ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }
}
