package com.indref.industrial_reforged.impl.tiers;

import com.indref.industrial_reforged.api.tiers.EnergyTier;

public record EnergyTierImpl(int maxInput, int maxOutput, int color, int defaultCapacity) implements EnergyTier {
    public EnergyTierImpl(int maxTransfer, int color, int defaultCapacity) {
        this(maxTransfer, maxTransfer, color, defaultCapacity);
    }
}
