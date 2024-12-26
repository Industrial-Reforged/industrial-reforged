package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;

public interface IEnergyStorage {
    Holder<EnergyTier> getEnergyTier();

    default void onEnergyChanged(int oldAmount) {
    }

    /**
     * Removes energy from the storage. Returns the amount of energy that was removed.
     *
     * @param value    The amount of energy being drained.
     * @param simulate whether the draining should only be simulated
     * @return Amount of energy that was extracted from the storage.
     */
    default int tryDrainEnergy(int value, boolean simulate) {
        if (!canDrainEnergy() || value <= 0) {
            return 0;
        }

        int energyExtracted = Math.min(getEnergyStored(), Math.min(getMaxOutput(), value));
        if (!simulate) {
            setEnergyStored(getEnergyStored() - energyExtracted);
        }
        return energyExtracted;
    }

    /**
     * Adds energy to the storage. Returns the amount of energy that was accepted.
     *
     * @param value    The amount of energy being filled.
     * @param simulate whether the filling should only be simulated
     * @return Amount of energy that was accepted by the storage.
     */
    default int tryFillEnergy(int value, boolean simulate) {
        if (!canFillEnergy() || value <= 0) {
            return 0;
        }

        int energyReceived = Mth.clamp(getEnergyCapacity() - getEnergyStored(), 0, Math.min(getMaxInput(), value));
        if (!simulate) {
            setEnergyStored(getEnergyStored() + energyReceived);
        }
        return energyReceived;
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
        return getEnergyTier().value().getMaxInput();
    }

    default int getMaxOutput() {
        return getEnergyTier().value().getMaxOutput();
    }

    default boolean canFillEnergy() {
        return getMaxInput() > 0;
    }

    default boolean canDrainEnergy() {
        return getMaxOutput() > 0;
    }
}
