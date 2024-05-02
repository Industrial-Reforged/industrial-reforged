package com.indref.industrial_reforged.api.data.energy;

public interface IEnergyStorage {
    int getEnergyStored();

    void setEnergyStored(int value);

    int getEnergyCapacity();

    void setEnergyCapacity(int value);
}
