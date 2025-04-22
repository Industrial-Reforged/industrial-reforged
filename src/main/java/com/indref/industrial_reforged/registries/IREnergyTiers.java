package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IREnergyTiers {
    public static final DeferredRegister<EnergyTier> ENERGY_TIERS = DeferredRegister.create(IRRegistries.ENERGY_TIER, IndustrialReforged.MODID);

    public static final Supplier<EnergyTier> NONE = ENERGY_TIERS.register("none", () -> new EnergyTier(0, ChatFormatting.GRAY.getColor()));
    public static final Supplier<EnergyTier> LOW = ENERGY_TIERS.register("low", () -> new EnergyTier(32, ChatFormatting.WHITE.getColor()));
    public static final Supplier<EnergyTier> MEDIUM = ENERGY_TIERS.register("medium", () -> new EnergyTier(128, ChatFormatting.GOLD.getColor()));
    public static final Supplier<EnergyTier> HIGH = ENERGY_TIERS.register("high", () -> new EnergyTier(512,ChatFormatting.BLUE.getColor()));
    public static final Supplier<EnergyTier> EXTREME = ENERGY_TIERS.register("extreme", () -> new EnergyTier(2_048, ChatFormatting.GREEN.getColor()));
    public static final Supplier<EnergyTier> INSANE = ENERGY_TIERS.register("insane", () -> new EnergyTier(8_192, ChatFormatting.RED.getColor()));
    public static final Supplier<EnergyTier> CREATIVE = ENERGY_TIERS.register("creative", () -> new EnergyTier(Integer.MAX_VALUE, ChatFormatting.LIGHT_PURPLE.getColor()));
}
