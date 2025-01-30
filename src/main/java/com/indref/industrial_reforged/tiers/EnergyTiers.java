package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class EnergyTiers {
    // TODO: Adjust these values
    public static final Holder<EnergyTier> NONE = register("none", ChatFormatting.GRAY.getColor(), 0, 0);
    public static final Holder<EnergyTier> LOW = register("low", ChatFormatting.WHITE.getColor(), 16, 4_000);
    public static final Holder<EnergyTier> MEDIUM = register("medium", ChatFormatting.GOLD.getColor(),64, 16_000);
    public static final Holder<EnergyTier> HIGH = register("high", ChatFormatting.BLUE.getColor(),128, 32_000);
    public static final Holder<EnergyTier> EXTREME = register("extreme", ChatFormatting.GREEN.getColor(), 512, 256_000);
    public static final Holder<EnergyTier> INSANE = register("insane", ChatFormatting.RED.getColor(), 1024, 512_000);
    public static final Holder<EnergyTier> CREATIVE = register("creative", ChatFormatting.LIGHT_PURPLE.getColor(), Integer.MAX_VALUE, Integer.MAX_VALUE);

    public static Holder<EnergyTier> register(String name, int color, int throughput, int capacity) {
        return register(name, color, throughput, throughput, capacity);
    }

    public static Holder<EnergyTier> register(String name, int color, int maxInput, int maxOutput, int capacity) {
        EnergyTier energyTier = new EnergyTier(name, maxInput, maxOutput, capacity, color);
        return Registry.registerForHolder(IRRegistries.ENERGY_TIER, IndustrialReforged.rl(name), energyTier);
    }
}
