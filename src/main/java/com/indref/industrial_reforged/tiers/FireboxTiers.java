package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.api.tiers.FireboxTier;

public enum FireboxTiers implements FireboxTier {
    REFRACTORY(15),
    SMALL(5);

    private final int maxHeatOut;

    FireboxTiers(int maxHeatOut) {
        this.maxHeatOut = maxHeatOut;
    }

    @Override
    public int getMaxHeatOutput() {
        return maxHeatOut;
    }
}
