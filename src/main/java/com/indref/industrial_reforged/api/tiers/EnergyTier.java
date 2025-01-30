package com.indref.industrial_reforged.api.tiers;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.network.chat.Component;

public record EnergyTier(String name, int maxInput, int maxOutput, int defaultCapacity, int color) {
    public Component getTranslation() {
        return Component.translatable("energy_tier." + IndustrialReforged.MODID + "." + name);
    }
}
