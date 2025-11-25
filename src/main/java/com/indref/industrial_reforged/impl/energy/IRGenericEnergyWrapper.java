package com.indref.industrial_reforged.impl.energy;

import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.portingdeadmods.portingdeadlibs.api.capabilities.EnergyStorageWrapper;

public record IRGenericEnergyWrapper(EnergyHandler energyStorage) implements EnergyStorageWrapper {
    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getEnergyCapacity() {
        return energyStorage.getEnergyCapacity();
    }
}
