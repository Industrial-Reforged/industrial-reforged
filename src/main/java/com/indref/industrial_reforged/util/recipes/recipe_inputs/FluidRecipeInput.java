package com.indref.industrial_reforged.util.recipes.recipe_inputs;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidRecipeInput(FluidStack fluidStack) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 0;
    }
}
