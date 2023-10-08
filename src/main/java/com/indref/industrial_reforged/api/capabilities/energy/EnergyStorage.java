package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import io.netty.util.IllegalReferenceCountException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Main file for handling storing and
 * modifying data of the energy capability
 */
public class EnergyStorage implements IEnergyStorage {
    public EnergyStorage() {
    }

    public EnergyStorage(ItemStack itemStack) {
        itemStack.serializeNBT().putInt(NBT_KEY_ENERGY_STORED, this.stored);
    }

    public int stored;
    public int capacity;

    private static final String NBT_KEY_ENERGY_STORED = "storedEnergy";
    private static final String NBT_KEY_MAX_ENERGY = "maxEnergy";

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
