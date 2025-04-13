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

    // TODO: Adjust these values
    public static final Supplier<EnergyTier> NONE = ENERGY_TIERS.register("none", () -> new EnergyTier(ChatFormatting.GRAY.getColor(), 0, 0, ChatFormatting.GRAY));
    public static final Supplier<EnergyTier> LOW = ENERGY_TIERS.register("low", () -> new EnergyTier(ChatFormatting.WHITE.getColor(), 16, 4_000, ChatFormatting.WHITE));
    public static final Supplier<EnergyTier> MEDIUM = ENERGY_TIERS.register("medium", () -> new EnergyTier(ChatFormatting.GOLD.getColor(),64, 16_000, ChatFormatting.GOLD));
    public static final Supplier<EnergyTier> HIGH = ENERGY_TIERS.register("high", () -> new EnergyTier(ChatFormatting.BLUE.getColor(),128, 32_000, ChatFormatting.BLUE));
    public static final Supplier<EnergyTier> EXTREME = ENERGY_TIERS.register("extreme", () -> new EnergyTier(ChatFormatting.GREEN.getColor(), 512, 64_000, ChatFormatting.GREEN));
    public static final Supplier<EnergyTier> INSANE = ENERGY_TIERS.register("insane", () -> new EnergyTier(ChatFormatting.RED.getColor(), 1024, 128_000, ChatFormatting.RED));
    public static final Supplier<EnergyTier> CREATIVE = ENERGY_TIERS.register("creative", () -> new EnergyTier(ChatFormatting.LIGHT_PURPLE.getColor(), Integer.MAX_VALUE, Integer.MAX_VALUE, ChatFormatting.LIGHT_PURPLE));
}
