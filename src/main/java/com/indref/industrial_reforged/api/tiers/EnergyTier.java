package com.indref.industrial_reforged.api.tiers;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public record EnergyTier(int maxInput, int maxOutput, int defaultCapacity, int color, @Nullable ChatFormatting chatFormatting) {
    public EnergyTier(int maxTransfer, int defaultCapacity, int color) {
        this(maxTransfer, maxTransfer, defaultCapacity, color, null);
    }
    public EnergyTier(int maxTransfer, int defaultCapacity, int color, @Nullable ChatFormatting chatFormatting) {
        this(maxTransfer, maxTransfer, defaultCapacity, color, chatFormatting);
    }
}
