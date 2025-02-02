package com.indref.industrial_reforged.api.capabilities.fluid;

import com.indref.industrial_reforged.util.Utils;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntList;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public record SidedFluidHandler(IFluidHandler innerHandler,
                                IOAction action,
                                IntList tanks) implements IFluidHandler {
    public SidedFluidHandler(IFluidHandler innerHandler, Pair<IOAction, int[]> actionSlotsPair) {
        this(innerHandler, actionSlotsPair != null ? actionSlotsPair.left() : IOAction.NONE, actionSlotsPair != null ? Utils.intArrayToList(actionSlotsPair.right()) : IntList.of());
    }

    @Override
    public int getTanks() {
        return innerHandler.getTanks();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return innerHandler.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return innerHandler.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return action == IOAction.INSERT || action == IOAction.BOTH && tanks.contains(tank) && innerHandler.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction fAction) {
        return action == IOAction.INSERT || action == IOAction.BOTH ? innerHandler.fill(resource, fAction) : 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction fAction) {
        return action == IOAction.EXTRACT || action == IOAction.BOTH ? innerHandler.drain(resource, fAction) : FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction fAction) {
        return action == IOAction.EXTRACT || action == IOAction.BOTH ? innerHandler.drain(maxDrain, fAction) : FluidStack.EMPTY;
    }
}
