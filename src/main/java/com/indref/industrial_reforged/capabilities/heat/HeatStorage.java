package com.indref.industrial_reforged.capabilities.heat;

import net.minecraft.nbt.CompoundTag;

public class HeatStorage implements IHeatStorage {

    public int stored;

    private static final String NBT_KEY_HEAT_STORED = "heatStored";

    @Override
    public int getHeatStored() {
        return this.stored;
    }

    @Override
    public void setHeatStored(int value) {
        this.stored = value;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_KEY_HEAT_STORED, this.stored);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.stored = nbt.getInt(NBT_KEY_HEAT_STORED);
    }
}
