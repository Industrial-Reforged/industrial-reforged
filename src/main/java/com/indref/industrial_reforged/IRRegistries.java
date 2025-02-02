package com.indref.industrial_reforged;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class IRRegistries {
    public static final ResourceKey<Registry<EnergyTier>> ENERGY_TIER_KEY =
            ResourceKey.createRegistryKey(IndustrialReforged.rl("energy_tier"));
    public static final Registry<EnergyTier> ENERGY_TIER =
            new RegistryBuilder<>(ENERGY_TIER_KEY).create();
}
