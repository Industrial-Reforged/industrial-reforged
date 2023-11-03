package com.indref.industrial_reforged.capabilities.energy.storage;

import net.neoforged.neoforge.common.capabilities.AutoRegisterCapability;

/**
 * Basic Capability Interface used for handling
 * methods related to the energy storage capability
 */
@AutoRegisterCapability
public interface IEnergyStorage {
    int getEnergyStored();
    void setEnergyStored(int value);
}
