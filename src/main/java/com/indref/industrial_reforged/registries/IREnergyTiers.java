package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IREnergyTiers {
    public static final DeferredRegister<EnergyTier> ENERGY_TIERS = DeferredRegister.create(IRRegistries.ENERGY_TIER, IndustrialReforged.MODID);

    public static final Supplier<EnergyTierImpl> NONE = ENERGY_TIERS.register("none", () -> new EnergyTierImpl(0, ChatFormatting.GRAY.getColor(), 0));
    public static final Supplier<EnergyTierImpl> LOW = ENERGY_TIERS.register("low", () -> new EnergyTierImpl(32, ChatFormatting.WHITE.getColor(), 4_000));
    public static final Supplier<EnergyTierImpl> MEDIUM = ENERGY_TIERS.register("medium", () -> new EnergyTierImpl(128, ChatFormatting.GOLD.getColor(), 16_000));
    public static final Supplier<EnergyTierImpl> HIGH = ENERGY_TIERS.register("high", () -> new EnergyTierImpl(512,ChatFormatting.BLUE.getColor(), 32_000));
    public static final Supplier<EnergyTierImpl> EXTREME = ENERGY_TIERS.register("extreme", () -> new EnergyTierImpl(2_048, ChatFormatting.GREEN.getColor(), 64_000));
    public static final Supplier<EnergyTierImpl> INSANE = ENERGY_TIERS.register("insane", () -> new EnergyTierImpl(8_192, ChatFormatting.RED.getColor(), 128_000));
    public static final Supplier<EnergyTierImpl> CREATIVE = ENERGY_TIERS.register("creative", () -> new EnergyTierImpl(Integer.MAX_VALUE, ChatFormatting.LIGHT_PURPLE.getColor(), Integer.MAX_VALUE));
}
