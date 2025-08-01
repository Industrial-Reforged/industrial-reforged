package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;

import java.util.function.Supplier;

public record SidedEnergyHandler(IEnergyHandler innerHandler, IOAction action) implements IEnergyHandler {
    public SidedEnergyHandler(IEnergyHandler innerHandler, Pair<IOAction, int[]> action) {
        this(innerHandler, action.first());
    }

    @Override
    public Supplier<EnergyTier> getEnergyTier() {
        return innerHandler.getEnergyTier();
    }

    @Override
    public int getEnergyStored() {
        return innerHandler.getEnergyStored();
    }

    @Override
    public void setEnergyStored(int value) {
        innerHandler.setEnergyStored(value);
    }

    @Override
    public int getEnergyCapacity() {
        return innerHandler.getEnergyCapacity();
    }

    @Override
    public void setEnergyCapacity(int value) {
        innerHandler.setEnergyCapacity(value);
    }

    @Override
    public int drainEnergy(int value, boolean simulate) {
        return action == IOAction.EXTRACT || action == IOAction.BOTH ? innerHandler.drainEnergy(value, simulate) : 0;
    }

    @Override
    public int fillEnergy(int value, boolean simulate) {
        return action == IOAction.INSERT || action == IOAction.BOTH ? innerHandler.fillEnergy(value, simulate) : 0;
    }
}
