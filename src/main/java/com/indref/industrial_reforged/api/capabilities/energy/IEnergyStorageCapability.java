package com.indref.industrial_reforged.api.capabilities.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
// TODO: 10/7/2023 use a better name for all energy related capabilities  
public interface IEnergyStorageCapability extends INBTSerializable<CompoundTag> {
    long getMaxEnergy();
    long getCurEnergy();
    void setCurEnergy(long value);
    void increaseCurEnergy(long value);
    void decreaseCurEnergy(long value);
}
