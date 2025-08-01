package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.capabilities.OnChangedListener;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IREnergyTiers;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnergyHandler implements IEnergyHandler, INBTSerializable<CompoundTag>, OnChangedListener {
    public static final EnergyHandler EMPTY = new EnergyHandler(IREnergyTiers.NONE);

    private final Supplier<EnergyTier> energyTier;
    private int energyStored;
    private int energyCapacity;

    private Consumer<Integer> onChangedFunction = oldAmount -> {};

    public EnergyHandler(Supplier<EnergyTier> energyTier) {
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

    public static class NoDrain extends EnergyHandler implements IEnergyHandler.NoDrain {
        public NoDrain(Supplier<EnergyTier> energyTier) {
            super(energyTier);
        }

        @Override
        public int drainEnergy(int value, boolean simulate) {
            return IEnergyHandler.NoDrain.super.drainEnergy(value, simulate);
        }

    }

    public static class NoFill extends EnergyHandler implements IEnergyHandler.NoFill {
        public NoFill(Supplier<EnergyTier> energyTier) {
            super(energyTier);
        }

        @Override
        public int drainEnergy(int value, boolean simulate) {
            return IEnergyHandler.NoFill.super.drainEnergy(value, simulate);
        }

    }

}
