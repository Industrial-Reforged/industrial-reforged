package com.indref.industrial_reforged.api.data.attachments;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

/**
 * Class for saving and loading heat values of item/block/entity
 * <br><br>
 * Should not be use directly,
 * only for attaching data/accessing it through capabilities
 */
public final class AttachmentHeatStorage implements INBTSerializable<CompoundTag> {
    private int heat;
    private int maxHeat;

    public AttachmentHeatStorage(int heat, int maxHeat) {
        this.heat = heat;
        this.maxHeat = maxHeat;
    }

    public int getHeatStored() {
        return heat;
    }

    public void setHeatStored(int heat) {
        this.heat = heat;
    }

    public int getHeatCapacity() {
        return maxHeat;
    }

    public void setHeatCapacity(int heat) {
        this.maxHeat = heat;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("heat", this.heat);
        tag.putInt("maxHeat", this.maxHeat);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.heat = tag.getInt("heat");
        this.maxHeat = tag.getInt("maxHeat");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttachmentHeatStorage that)) return false;
        return heat == that.heat && maxHeat == that.maxHeat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(heat, maxHeat);
    }
}
