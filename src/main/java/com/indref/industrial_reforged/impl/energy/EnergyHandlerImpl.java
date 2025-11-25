package com.indref.industrial_reforged.impl.energy;

import com.indref.industrial_reforged.api.capabilities.StorageChangedListener;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;
import com.indref.industrial_reforged.registries.IREnergyTiers;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnergyHandlerImpl implements EnergyHandler, INBTSerializable<CompoundTag>, StorageChangedListener {
    public static final EnergyHandlerImpl EMPTY = new EnergyHandlerImpl(IREnergyTiers.NONE);

    private final Supplier<EnergyTierImpl> energyTier;
    private int energyStored;
    private int energyCapacity;

    private Consumer<Integer> onChangedFunction = oldAmount -> {};

    public EnergyHandlerImpl(Supplier<EnergyTierImpl> energyTier) {
        this.energyTier = energyTier;
    }

    @Override
    public void setOnChangedFunction(Consumer<Integer> onChangedFunction) {
        this.onChangedFunction = onChangedFunction;
    }

    @Override
    public void onChanged(int oldAmount) {
        this.onChangedFunction.accept(oldAmount);
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
            onChanged(stored);
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
        return energyTier.get();
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

    public static class NoDrain extends EnergyHandlerImpl implements EnergyHandler.NoDrain {
        public NoDrain(Supplier<EnergyTierImpl> energyTier) {
            super(energyTier);
        }

        @Override
        public int drainEnergy(int value, boolean simulate) {
            return EnergyHandler.NoDrain.super.drainEnergy(value, simulate);
        }

    }

    public static class NoFill extends EnergyHandlerImpl implements EnergyHandler.NoFill {
        public NoFill(Supplier<EnergyTierImpl> energyTier) {
            super(energyTier);
        }

        @Override
        public int drainEnergy(int value, boolean simulate) {
            return EnergyHandler.NoFill.super.drainEnergy(value, simulate);
        }

    }

}
