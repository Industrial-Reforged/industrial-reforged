package com.indref.industrial_reforged.api.capabilities;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IEnergyCapability {
    long getMaxEnergy();
    long getCurEnergy();
    void setCurEnergy(long value);
    void increaseCurEnergy(long value);
    void decreaseCurEnergy(long value);
}
