package com.indref.industrial_reforged.api.util;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Functional interfaces for itemstack and fluidstack validation checks
 */
public class ValidationFunctions {
    @FunctionalInterface
    public interface ItemValid {
        boolean itemValid(int slot, ItemStack itemStack);
    }

    @FunctionalInterface
    public interface FluidValid {
        boolean fluidValid(FluidStack fluidStack);
    }
}
