package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.tiers.EnergyTier;

public interface IEnergyStorage {
    EnergyTier getEnergyTier();

    boolean tryDrainEnergy(int value);

    boolean tryFillEnergy(int value);

    int getEnergyStored();

    void setEnergyStored(int value);

    int getEnergyCapacity();

    void setEnergyCapacity(int value);

    default boolean canAcceptEnergy(int amount) {
        return getEnergyStored() + amount <= getEnergyCapacity();
    }
}
