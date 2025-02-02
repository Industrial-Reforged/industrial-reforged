package com.indref.industrial_reforged.util.recipes.recipeInputs;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CrucibleCastingRecipeInput(ItemStack moldItem, FluidStack fluidStack) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int i) {
        return i == 0 ? moldItem : ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }
}
