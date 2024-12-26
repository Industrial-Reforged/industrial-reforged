package com.indref.industrial_reforged.api.capabilities.heat;

import net.minecraft.util.Mth;

public interface IHeatStorage {
    default void onHeatChanged(int oldAmount) {
    }

    int getHeatStored();

    void setHeatStored(int value);

    int getHeatCapacity();

    void setHeatCapacity(int value);

    default int tryDrainHeat(int value, boolean simulate) {
        if (!canDrainHeat() || value <= 0) {
            return 0;
        }

        int energyExtracted = Math.min(getHeatStored(), Math.min(getMaxOutput(), value));
        if (!simulate) {
            setHeatStored(getHeatStored() - energyExtracted);
        }
        return energyExtracted;
    }

    default int tryFillHeat(int value, boolean simulate) {
        if (!canFillHeat() || value <= 0) {
            return 0;
        }

        int energyReceived = Mth.clamp(getHeatCapacity() - getHeatStored(), 0, Math.min(getMaxInput(), value));
        if (!simulate) {
            setHeatStored(getHeatStored() + energyReceived);
        }
        return energyReceived;
    }

    int getMaxInput();

    int getMaxOutput();

    default boolean canFillHeat() {
        return getMaxInput() > 0;
    }

    default boolean canDrainHeat() {
        return getMaxOutput() > 0;
    }
}
