package com.indref.industrial_reforged.api.energy.blocks;

import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;

/**
 * Interface for implementing Blocks that store EU
 */
public interface IEnergyBlock {
    EnergyStorageProvider getEnergyStorage();
}
