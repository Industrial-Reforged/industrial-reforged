package com.indref.industrial_reforged.api.capabilities.heat.storage;

import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
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
            this.heatStored = value;
            onHeatChanged();
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

    public void onHeatChanged() {
    }

    @Override
    public int tryDrainHeat(int value) {
        if (getHeatStored() - value >= 0) {
            setHeatStored(getHeatStored() - value);
            return 0;
        }
        int stored = getHeatStored();
        setHeatStored(0);
        return value - stored;
    }

    @Override
    public int tryFillHeat(int value) {
        if (getHeatStored() + value <= getHeatCapacity()) {
            setHeatStored(getHeatStored() + value);
            return 0;
        }
        int stored = getHeatStored();
        setHeatStored(getHeatCapacity());
        return value - stored;
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
