package com.indref.industrial_reforged.api.capabilities.heat;

public interface IHeatStorage {
    int getHeatStored();
    void setHeatStored(int value);
    int getHeatCapacity();
    void setHeatCapacity(int value);
}
