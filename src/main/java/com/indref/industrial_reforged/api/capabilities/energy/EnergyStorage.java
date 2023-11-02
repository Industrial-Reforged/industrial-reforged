package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Main file for handling storing and
 * modifying data of the energy capability
 * For the api look at
 * {@link IEnergyItem} and
 * {@link IEnergyBlock}
 * <p>
 * Or use the {@link EnergyStorageProvider} and subscribe to the right {{@link net.neoforged.neoforge.event.AttachCapabilitiesEvent}}
 */
public class EnergyStorage implements IEnergyStorage {
    public int stored;

    private static final String NBT_KEY_STORED_ENERGY = "energyStored";

    @Override
    public int getEnergyStored() {
        return this.stored;
    }

    @Override
    public void setEnergyStored(int value) {
        this.stored = value;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_KEY_STORED_ENERGY, this.stored);
        tag.putInt(NBT_KEY_ENERGY_CAPACITY, this.capacity);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.stored = nbt.getInt(NBT_KEY_STORED_ENERGY);
    }
}
