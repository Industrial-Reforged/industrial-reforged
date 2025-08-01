package com.indref.industrial_reforged.api.capabilities.energy;

import com.portingdeadmods.portingdeadlibs.api.capabilities.EnergyStorageWrapper;

public class IREnergyStorageWrapper implements EnergyStorageWrapper {
    private final IEnergyHandler energyStorage;

    public IREnergyStorageWrapper(IEnergyHandler energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getEnergyCapacity() {
        return energyStorage.getEnergyCapacity();
    }
}
