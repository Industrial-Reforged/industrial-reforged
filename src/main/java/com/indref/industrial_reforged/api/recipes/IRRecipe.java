package com.indref.industrial_reforged.api.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

public interface IRRecipe<T extends Container> extends Recipe<T> {
    @Override
    default @NotNull ItemStack assemble(@NotNull T container, HolderLookup.Provider provider) {
        return getResultItem(provider).copy();
    }

    @Override
    default boolean canCraftInDimensions(int i, int i1) {
        return true;
    }
}
