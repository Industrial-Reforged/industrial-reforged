package com.indref.industrial_reforged.api.data.attachments;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public final class AttachmentEnergyStorage implements INBTSerializable<CompoundTag> {
    private int energyStored;
    private int energyCapacity;

    public AttachmentEnergyStorage(int energyStored, int energyCapacity){
        this.energyCapacity = energyCapacity;
        this.energyStored = energyStored;
    }

    public int getEnergyStored() {
        return energyStored;
    }

    public int getEnergyCapacity() {
        return energyCapacity;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("energy_stored", this.energyStored);
        tag.putInt("energy_capacity", this.energyCapacity);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.energyStored = tag.getInt("energy_stored");
        this.energyCapacity = tag.getInt("energy_capacity");
    }
}
