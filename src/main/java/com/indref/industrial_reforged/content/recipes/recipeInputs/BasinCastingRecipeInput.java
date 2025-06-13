package com.indref.industrial_reforged.content.recipes.recipeInputs;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record BasinCastingRecipeInput(ItemStack catalystItem, FluidStack fluidStack) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int i) {
        return i == 0 ? catalystItem : ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }
}
