package com.indref.industrial_reforged.api.energy;

import com.indref.industrial_reforged.api.capabilities.EnergyStorage;

public interface IEnergyStorage {
    EnergyStorage getEnergyStorage();


    // These are just wrappers for energy storage methods
    default void increaseCurEnergy(long value) {
        getEnergyStorage().increaseCurEnergy(value);
    }

    default void decreaseCurEnergy(long value) {
        getEnergyStorage().decreaseCurEnergy(value);
    }

}
