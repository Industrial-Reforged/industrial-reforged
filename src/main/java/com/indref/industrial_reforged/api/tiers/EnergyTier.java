package com.indref.industrial_reforged.api.tiers;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.network.chat.Component;

public record EnergyTier(int maxInput, int maxOutput, int defaultCapacity, int color) {
    public EnergyTier(int maxTransfer, int defaultCapacity, int color) {
        this(maxTransfer, maxTransfer, defaultCapacity, color);
    }
}
