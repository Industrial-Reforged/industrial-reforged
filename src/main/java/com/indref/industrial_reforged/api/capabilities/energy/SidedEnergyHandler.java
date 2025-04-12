package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Holder;

import java.util.function.Supplier;

public record SidedEnergyHandler(IEnergyStorage innerHandler, IOAction action) implements IEnergyStorage {
    public SidedEnergyHandler(IEnergyStorage innerHandler, Pair<IOAction, int[]> action) {
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
    public int tryDrainEnergy(int value, boolean simulate) {
        return action == IOAction.EXTRACT || action == IOAction.BOTH ? innerHandler.tryDrainEnergy(value, simulate) : 0;
    }

    @Override
    public int tryFillEnergy(int value, boolean simulate) {
        return action == IOAction.INSERT || action == IOAction.BOTH ? innerHandler.tryFillEnergy(value, simulate) : 0;
    }
}
