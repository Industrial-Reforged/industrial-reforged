package com.indref.industrial_reforged.content.recipes.recipeInputs;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record ItemFluidRecipeInput(ItemStack itemStack, FluidStack fluidStack) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int i) {
        return i == 0 ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }
}
