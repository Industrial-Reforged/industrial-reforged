package com.indref.industrial_reforged.api.capabilities.heat;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public class HeatStorage implements IHeatStorage, INBTSerializable<CompoundTag> {
    public static final HeatStorage EMPTY = new HeatStorage(0, 0, 0);

    private float heatStored;
    private float heatCapacity;

    private final float maxInput;
    private final float maxOutput;

    public HeatStorage(float heatCapacity) {
        this.heatCapacity = heatCapacity;
        this.maxInput = 5;
        this.maxOutput = 5;
    }

    public HeatStorage(float heatCapacity, float maxInput, float maxOutput) {
        this.heatCapacity = heatCapacity;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
    }

    @Override
    public float getHeatStored() {
        return this.heatStored;
    }

    @Override
    public void setHeatStored(float value) {
        if (this.heatStored != value) {
            float oldAmount = this.heatStored;
            this.heatStored = value;
            onHeatChanged(oldAmount);
        }
    }

    @Override
    public float getHeatCapacity() {
        return this.heatCapacity;
    }

    @Override
    public void setHeatCapacity(float value) {
        if (this.heatCapacity != value) {
            this.heatCapacity = value;
        }
    }

    @Override
    public float getMaxInput() {
        return this.maxInput;
    }

    @Override
    public float getMaxOutput() {
        return this.maxOutput;
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("heat_stored", this.heatStored);
        tag.putFloat("heat_capacity", this.heatCapacity);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag tag) {
        this.heatStored = tag.getFloat("heat_stored");
        this.heatCapacity = tag.getFloat("heat_capacity");
    }
}
