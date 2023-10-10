package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.energy.items.IEnergyItem;
import com.indref.industrial_reforged.networking.IRPackets;
import com.indref.industrial_reforged.networking.packets.S2CEnergyItem;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

/**
 * Main file for handling storing and
 * modifying data of the energy capability
 * For the api look at
 * {@link com.indref.industrial_reforged.api.energy.items.IEnergyItem} and
 * {@link com.indref.industrial_reforged.api.energy.blocks.IEnergyBlock}
 * <p>
 * Or use the {@link EnergyStorageProvider} and subscribe to the right {@link net.minecraftforge.event.AttachCapabilitiesEvent}
 */
public class EnergyStorage implements IEnergyStorage {
    ItemStack itemStack = ItemStack.EMPTY;
    public EnergyStorage() {
    }

    public EnergyStorage(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IEnergyItem energyItem) {
            this.capacity = energyItem.getMaxEnergy();
            this.itemStack = itemStack;
        }
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
        if (!this.itemStack.equals(ItemStack.EMPTY)) {
            IRPackets.sendToClients(new S2CEnergyItem(getEnergyStored(), this.itemStack));
        }
        this.stored = value;
    }

    @Override
    public void setMaxEnergy(int value) {
        this.capacity = value;
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
