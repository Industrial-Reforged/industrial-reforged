package com.indref.industrial_reforged.api.tiers;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public record EnergyTier(int maxInput, int maxOutput, int color) {
    public EnergyTier(int maxTransfer, int color) {
        this(maxTransfer, maxTransfer, color);
    }
}
