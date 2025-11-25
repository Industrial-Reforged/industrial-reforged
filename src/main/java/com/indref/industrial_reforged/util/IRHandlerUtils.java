package com.indref.industrial_reforged.util;

import com.portingdeadmods.portingdeadlibs.api.capabilities.DynamicFluidTank;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

public final class IRHandlerUtils {
    public static FluidTank newFluidTank(BiPredicate<Integer, FluidStack> fluidValidator, Int2IntFunction tankCapacity, Consumer<Integer> onChange, int slots) {
        return new FluidTank(tankCapacity.applyAsInt(0)) {
            @Override
            public int getTankCapacity(int tank) {
                return tankCapacity.applyAsInt(tank);
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return fluidValidator.test(0, stack);
            }

            @Override
            protected void onContentsChanged() {
                onChange.accept(0);
            }
        };
    }

    public static DynamicFluidTank newDynamicFluidTank(BiPredicate<Integer, FluidStack> fluidValidator, Int2IntFunction tankCapacity, Consumer<Integer> onChange, int slots) {
        return new DynamicFluidTank(tankCapacity.applyAsInt(0)) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return fluidValidator.test(0, stack);
            }

            @Override
            protected void onContentsChanged() {
                onChange.accept(0);
            }
        };
    }
}
