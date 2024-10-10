package com.indref.industrial_reforged.api.capabilities.heat;

import com.indref.industrial_reforged.api.capabilities.IOActions;

public record SidedHeatHandler(IHeatStorage innerHandler, IOActions action) implements IHeatStorage {
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
        return action == IOActions.EXTRACT || action == IOActions.BOTH ? innerHandler.tryDrainHeat(value, simulate) : 0;
    }

    @Override
    public int tryFillHeat(int value, boolean simulate) {
        return action == IOActions.INSERT || action == IOActions.BOTH ? innerHandler.tryFillHeat(value, simulate) : 0;
    }
}
