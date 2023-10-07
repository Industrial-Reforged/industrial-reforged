package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.nbt.CompoundTag;

public class EnergyStorage implements IEnergyStorage {
    public int stored;
    public int capacity;

    private static final String NBT_KEY_ENERGY_STORED = "storedEnergy";
    private static final String NBT_KEY_MAX_ENERGY = "maxEnergy";

    public EnergyStorage() {
    }

    @Override
    public int getEnergyStored() {
        return this.stored;
    }

    @Override
    public int getMaxEnergy() {
        return this.capacity;
    }

    @Override
    public void setEnergyStored(int value) {
        this.stored = value;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt(NBT_KEY_ENERGY_STORED, this.stored);
    }

    public void loadNBTData(CompoundTag nbt) {
        IndustrialReforged.LOGGER.info("Loaded energy capability");
        this.stored = nbt.getInt(NBT_KEY_ENERGY_STORED);
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_KEY_ENERGY_STORED, this.stored);
        tag.putInt(NBT_KEY_MAX_ENERGY, this.capacity);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.stored = nbt.getInt(NBT_KEY_ENERGY_STORED);
    }
}
