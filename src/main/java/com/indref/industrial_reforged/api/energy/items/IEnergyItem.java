package com.indref.industrial_reforged.api.energy.items;

import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;

/**
 * Interface for implementing Items that store EU
 */
public interface IEnergyItem {
    EnergyStorageProvider getEnergyStorage();
}
