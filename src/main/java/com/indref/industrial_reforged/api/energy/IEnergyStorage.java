package com.indref.industrial_reforged.api.energy;

import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageCapability;

public interface IEnergyStorage {
    EnergyStorageCapability getEnergyStorage();

    // These are just wrappers for energy storage methods
    default void increaseCurEnergy(long value) {
        getEnergyStorage().increaseCurEnergy(value);
    }

    default void decreaseCurEnergy(long value) {
        getEnergyStorage().decreaseCurEnergy(value);
    }

    default long getCurEnergy() {
        return getEnergyStorage().getCurEnergy();
    }

    default long getMaxEnergy() {
        return getEnergyStorage().getMaxEnergy();
    }
}
