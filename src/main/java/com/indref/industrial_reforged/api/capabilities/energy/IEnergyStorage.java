package com.indref.industrial_reforged.api.capabilities.energy;

import net.neoforged.neoforge.common.capabilities.AutoRegisterCapability;

/**
 * Basic Capability Interface used for handling
 * methods related to the energy storage capability
 */
@AutoRegisterCapability
public interface IEnergyStorage {
    int getEnergyStored();

    int getEnergyCapacity();

    void setEnergyStored(int value);
}
