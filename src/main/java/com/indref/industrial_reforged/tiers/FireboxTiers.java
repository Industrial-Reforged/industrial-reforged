package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.api.tiers.FireboxTier;

public enum FireboxTiers implements FireboxTier {
    REFRACTORY(20.85f),
    SMALL(2.23f);

    private final float maxHeatOut;

    FireboxTiers(float maxHeatOut) {
        this.maxHeatOut = maxHeatOut;
    }

    @Override
    public float getMaxHeatOutput() {
        return maxHeatOut;
    }
}
