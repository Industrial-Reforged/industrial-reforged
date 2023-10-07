package com.indref.industrial_reforged.api.capabilities.energy;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IEnergyStorage {
    int getEnergyStored();

    int getMaxEnergy();

    void setEnergyStored(int value);
}
