package com.indref.industrial_reforged.capabilities.heat;

import net.neoforged.neoforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IHeatStorage {
    int getHeatStored();
    void setHeatStored(int value);
}
