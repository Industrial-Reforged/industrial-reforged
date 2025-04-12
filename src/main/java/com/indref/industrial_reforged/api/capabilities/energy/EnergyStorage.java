package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IREnergyTiers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class EnergyStorage implements IEnergyStorage, INBTSerializable<CompoundTag> {
    public static final EnergyStorage EMPTY = new EnergyStorage(IREnergyTiers.NONE);

    private final Supplier<EnergyTier> energyTier;
    private int energyStored;
    private int energyCapacity;

    public EnergyStorage(Supplier<EnergyTier> energyTier) {
        this.energyTier = energyTier;
    }

    @Override
    public int getEnergyStored() {
        return this.energyStored;
    }

    @Override
    public void setEnergyStored(int value) {
        if (this.energyStored != value) {
            int stored = energyStored;
            this.energyStored = value;
            onEnergyChanged(stored);
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
    public Supplier<EnergyTier> getEnergyTier() {
        return energyTier;
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
