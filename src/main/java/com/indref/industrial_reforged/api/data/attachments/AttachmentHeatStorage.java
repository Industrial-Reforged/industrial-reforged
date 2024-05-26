package com.indref.industrial_reforged.api.data.attachments;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Class for saving and loading heat values of item/block/entity
 * <br><br>
 * Should not be use directly,
 * only for attaching data/accessing it through capabilities
 */
public final class AttachmentHeatStorage implements INBTSerializable<CompoundTag> {
    private int heatStored;
    private int heatCapacity;

    public AttachmentHeatStorage(int heatStored, int heatCapacity) {
        this.heatStored = heatStored;
        this.heatCapacity = heatCapacity;
    }

    public int getHeatStored() {
        return heatStored;
    }

    public int getHeatCapacity() {
        return heatCapacity;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("heat_stored", this.heatStored);
        tag.putInt("heat_capacity", this.heatCapacity);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag tag) {
        this.heatStored = tag.getInt("heat_stored");
        this.heatCapacity = tag.getInt("heat_capacity");
    }
}
