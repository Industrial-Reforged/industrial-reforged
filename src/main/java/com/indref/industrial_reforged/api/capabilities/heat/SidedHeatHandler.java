package com.indref.industrial_reforged.api.capabilities.heat;

import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;

public record SidedHeatHandler(IHeatStorage innerHandler, IOAction action) implements IHeatStorage {
    public SidedHeatHandler(IHeatStorage innerHandler, Pair<IOAction, int[]> action) {
        this(innerHandler, action.first());
    }

    @Override
    public int getHeatStored() {
        return innerHandler.getHeatStored();
    }

    @Override
    public void setHeatStored(int value) {
        innerHandler.setHeatStored(value);
    }

    @Override
    public int getHeatCapacity() {
        return innerHandler.getHeatCapacity();
    }

    @Override
    public void setHeatCapacity(int value) {
        innerHandler.setHeatCapacity(value);
    }

    @Override
    public int getMaxInput() {
        return innerHandler.getMaxInput();
    }

    @Override
    public int getMaxOutput() {
        return innerHandler.getMaxOutput();
    }

    @Override
    public int tryDrainHeat(int value, boolean simulate) {
        return action == IOAction.EXTRACT || action == IOAction.BOTH ? innerHandler.tryDrainHeat(value, simulate) : 0;
    }

    @Override
    public int tryFillHeat(int value, boolean simulate) {
        return action == IOAction.INSERT || action == IOAction.BOTH ? innerHandler.tryFillHeat(value, simulate) : 0;
    }
}
