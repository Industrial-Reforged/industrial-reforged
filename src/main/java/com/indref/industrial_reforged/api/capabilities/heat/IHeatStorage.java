package com.indref.industrial_reforged.api.capabilities.heat;

import net.minecraft.util.Mth;

public interface IHeatStorage {
    default void onHeatChanged(float oldAmount) {
    }

    float getHeatStored();

    void setHeatStored(float value);

    float getLastHeatStored();

    void setLastHeatStored(float value);

    float getHeatCapacity();

    void setHeatCapacity(float value);

    /**
     * @return The heat that was extracted
     */
    default float drain(float value, boolean simulate) {
        if (!canDrain() || value <= 0) {
            return 0;
        }

        float heatExtracted = Math.min(getHeatStored(), Math.min(getMaxOutput(), value));
        if (!simulate) {
            setHeatStored(getHeatStored() - heatExtracted);
        }
        return heatExtracted;
    }

    /**
     * @return The heat that was actually filled
     */
    default float fill(float value, boolean simulate) {
        if (!canFill() || value <= 0) {
            return 0;
        }

        float heatReceived = Mth.clamp(getHeatCapacity() - getHeatStored(), 0, Math.min(getMaxInput(), value));
        if (!simulate) {
            setHeatStored(getHeatStored() + heatReceived);
        }
        return heatReceived;
    }

    float getMaxInput();

    float getMaxOutput();

    default boolean canFill() {
        return getMaxInput() > 0;
    }

    default boolean canDrain() {
        return getMaxOutput() > 0;
    }
}
