package com.indref.industrial_reforged.api.capabilities.energy;

public interface IEnergyStorage {
    boolean tryDrainEnergy(int value);
    boolean tryFillEnergy(int value);
    int getEnergyStored();
    void setEnergyStored(int value);
    int getEnergyCapacity();
    void setEnergyCapacity(int value);
    default boolean canAcceptEnergy(int amount) {
        return getEnergyStored() + amount < getEnergyCapacity();
    }
}
