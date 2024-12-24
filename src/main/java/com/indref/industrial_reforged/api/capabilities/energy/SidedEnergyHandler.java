package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;

public record SidedEnergyHandler(IEnergyStorage innerHandler, IOActions action) implements IEnergyStorage {
    @Override
    public Holder<EnergyTier> getEnergyTier() {
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
        return action == IOActions.EXTRACT || action == IOActions.BOTH ? innerHandler.tryDrainEnergy(value, simulate) : 0;
    }

    @Override
    public int tryFillEnergy(int value, boolean simulate) {
        return action == IOActions.EXTRACT || action == IOActions.BOTH ? innerHandler.tryFillEnergy(value, simulate) : 0;
    }
}
