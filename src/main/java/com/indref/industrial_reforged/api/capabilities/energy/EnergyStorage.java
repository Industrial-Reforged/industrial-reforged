package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public class EnergyStorage implements IEnergyStorage, INBTSerializable<CompoundTag> {
    public static final EnergyStorage EMPTY = new EnergyStorage(EnergyTiers.NONE);

    private final EnergyTier energyTier;
    private int energyStored;
    private int energyCapacity;

    public EnergyStorage(EnergyTier energyTier) {
        this.energyTier = energyTier;
    }

    @Override
    public int getEnergyStored() {
        return this.energyStored;
    }

    @Override
    public void setEnergyStored(int value) {
        if (this.energyStored != value) {
            this.energyStored = value;
            onEnergyChanged();
        }
    }

    @Override
    public int getEnergyCapacity() {
        return this.energyCapacity;
    }

    @Override
    public void setEnergyCapacity(int value) {
        if (this.energyCapacity != value) {
            this.energyCapacity = value;
        }
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier;
    }

    public void onEnergyChanged() {
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("energy_stored", this.energyStored);
        tag.putInt("energy_capacity", this.energyCapacity);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.energyStored = tag.getInt("energy_stored");
        this.energyCapacity = tag.getInt("energy_capacity");
    }
}
