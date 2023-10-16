package com.indref.industrial_reforged.api.capabilities.heat;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IHeatStorage {
    int getHeatStored();

    int getHeatCapacity();

    void setHeatStored(int value);
}
