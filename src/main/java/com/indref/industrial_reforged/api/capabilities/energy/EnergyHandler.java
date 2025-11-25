package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;
import net.minecraft.util.Mth;

import java.util.function.Supplier;

public interface EnergyHandler {
    EnergyTier getEnergyTier();

    default void onChanged(int oldAmount) {
    }

    /**
     * Removes energy from the storage. Returns the amount of energy that was removed.
     *
     * @param value    The amount of energy being drained.
     * @param simulate whether the draining should only be simulated
     * @return Amount of energy that was drained from the storage.
     */
    default int drainEnergy(int value, boolean simulate) {
        if (!canDrainEnergy() || value <= 0) {
            return 0;
        }

        int energyDrained = Math.min(getEnergyStored(), Math.min(getMaxOutput(), value));
        if (!simulate) {
            setEnergyStored(getEnergyStored() - energyDrained);
        }
        return energyDrained;
    }

    default int forceDrainEnergy(int value, boolean simulate) {
        if (!canDrainEnergy() || value <= 0) {
            return 0;
        }

        int energyDrained = Math.min(getEnergyStored(), value);
        if (!simulate) {
            setEnergyStored(getEnergyStored() - energyDrained);
        }
        return energyDrained;
    }

    /**
     * Adds energy to the storage. Returns the amount of energy that was accepted.
     *
     * @param value    The amount of energy being filled.
     * @param simulate whether the filling should only be simulated
     * @return Amount of energy that was accepted by the storage.
     */
    default int fillEnergy(int value, boolean simulate) {
        if (!canFillEnergy() || value <= 0) {
            return 0;
        }

        int energyFilled = Mth.clamp(getEnergyCapacity() - getEnergyStored(), 0, value);
        if (!simulate) {
            setEnergyStored(getEnergyStored() + energyFilled);
        }
        return energyFilled;
    }

    default int forceFillEnergy(int value, boolean simulate) {
        if (!canFillEnergy() || value <= 0) {
            return 0;
        }

        int energyFilled = Mth.clamp(getEnergyCapacity() - getEnergyStored(), 0, Math.min(getMaxInput(), value));
        if (!simulate) {
            setEnergyStored(getEnergyStored() + energyFilled);
        }
        return energyFilled;
    }

    /**
     * @return the amount of energy currently stored
     */
    int getEnergyStored();

    /**
     * Sets the energy currently stored to the provided amount
     *
     * @param value the new amount of energy
     */
    void setEnergyStored(int value);

    /**
     * @return the maximum amount of energy that can be stored
     */
    int getEnergyCapacity();

    /**
     * Sets the maximum amount of energy that can be stored to the provided value
     *
     * @param value the new maximum amount of energy that can be stored
     */
    void setEnergyCapacity(int value);

    default int getMaxInput() {
        return getEnergyTier().maxInput();
    }

    default int getMaxOutput() {
        return getEnergyTier().maxOutput();
    }

    default boolean canFillEnergy() {
        return getMaxInput() > 0;
    }

    default boolean canDrainEnergy() {
        return getMaxOutput() > 0;
    }

    interface NoDrain extends EnergyHandler {
        @Override
        default int drainEnergy(int value, boolean simulate) {
            return 0;
        }
    }

    interface NoFill extends EnergyHandler {
        @Override
        default int fillEnergy(int value, boolean simulate) {
            return 0;
        }
    }

}
