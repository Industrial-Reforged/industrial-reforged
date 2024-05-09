package com.indref.industrial_reforged.api.data.attachments;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public final class AttachmentEnergyStorage implements INBTSerializable<Tag> {
    private int energy;
    private int energyCapacity;

    public AttachmentEnergyStorage(int energy, int energyCapacity){
        this.energyCapacity = energyCapacity;
        this.energy = energy;
    }

    public int getEnergyStored() {
        return energy;
    }

    public int getEnergyCapacity() {
        return energyCapacity;
    }

    @Override
    public @UnknownNullability Tag serializeNBT(HolderLookup.Provider provider) {
        return IntTag.valueOf(this.energy);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        if (!(tag instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energy = intNbt.getAsInt();
    }
}
