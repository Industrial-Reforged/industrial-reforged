package com.indref.industrial_reforged.api.capabilities.heat;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public class HeatStorage implements IHeatStorage, INBTSerializable<CompoundTag> {
    public static final HeatStorage EMPTY = new HeatStorage();

    private int heatStored;
    private int heatCapacity;

    @Override
    public int getHeatStored() {
        return this.heatStored;
    }

    @Override
    public void setHeatStored(int value) {
        if (this.heatStored != value) {
            int oldAmount = this.heatStored;
            this.heatStored = value;
            onHeatChanged(oldAmount);
        }
    }

    @Override
    public int getHeatCapacity() {
        return this.heatCapacity;
    }

    @Override
    public void setHeatCapacity(int value) {
        if (this.heatCapacity != value) {
            this.heatCapacity = value;
        }
    }

    @Override
    public int getMaxInput() {
        return 20;
    }

    @Override
    public int getMaxOutput() {
        return 20;
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
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
