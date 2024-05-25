package com.indref.industrial_reforged.api.data.attachments;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

/**
 * Class for saving and loading heat values of item/block/entity
 * <br><br>
 * Should not be use directly,
 * only for attaching data/accessing it through capabilities
 */
public final class AttachmentHeatStorage implements INBTSerializable<Tag> {
    private int heat;
    private int maxHeat;

    public AttachmentHeatStorage(int heat, int maxHeat) {
        this.heat = heat;
        this.maxHeat = maxHeat;
    }

    public int getHeatStored() {
        return heat;
    }

    public int getHeatCapacity() {
        return maxHeat;
    }

    @Override
    public @UnknownNullability Tag serializeNBT(HolderLookup.Provider provider) {
        return IntTag.valueOf(this.heat);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        if (!(tag instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.heat = intNbt.getAsInt();
    }
}
