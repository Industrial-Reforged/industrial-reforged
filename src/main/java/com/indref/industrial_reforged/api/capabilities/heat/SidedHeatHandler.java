package com.indref.industrial_reforged.api.capabilities.heat;

import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;

public record SidedHeatHandler(IHeatStorage innerHandler, IOAction action) implements IHeatStorage {
    public SidedHeatHandler(IHeatStorage innerHandler, Pair<IOAction, int[]> action) {
        this(innerHandler, action.first());
    }

    @Override
    public float getHeatStored() {
        return innerHandler.getHeatStored();
    }

    @Override
    public void setHeatStored(float value) {
        innerHandler.setHeatStored(value);
    }

    @Override
    public float getHeatCapacity() {
        return innerHandler.getHeatCapacity();
    }

    @Override
    public void setHeatCapacity(float value) {
        innerHandler.setHeatCapacity(value);
    }

    @Override
    public float getMaxInput() {
        return innerHandler.getMaxInput();
    }

    @Override
    public float getMaxOutput() {
        return innerHandler.getMaxOutput();
    }

    @Override
    public float drain(float value, boolean simulate) {
        return action == IOAction.EXTRACT || action == IOAction.BOTH ? innerHandler.drain(value, simulate) : 0;
    }

    @Override
    public float fill(float value, boolean simulate) {
        return action == IOAction.INSERT || action == IOAction.BOTH ? innerHandler.fill(value, simulate) : 0;
    }
}
