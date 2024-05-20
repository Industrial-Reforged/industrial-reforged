package com.indref.industrial_reforged.api.data.attachments;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public final class AttachmentEnergyStorage implements INBTSerializable<Tag> {
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
    public @UnknownNullability Tag serializeNBT(HolderLookup.Provider provider) {
        return IntTag.valueOf(this.energyStored);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        if (!(tag instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energyStored = intNbt.getAsInt();
    }
}
