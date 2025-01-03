package com.indref.industrial_reforged;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class IRRegistries {
    private static final ResourceKey<Registry<Multiblock>> MULTIBLOCK_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "multiblock"));
    public static final Registry<Multiblock> MULTIBLOCK =
            new RegistryBuilder<>(MULTIBLOCK_KEY).create();

    public static final ResourceKey<Registry<EnergyTier>> ENERGY_TIER_KEY =
            ResourceKey.createRegistryKey(IndustrialReforged.rl("energy_tier"));
    public static final Registry<EnergyTier> ENERGY_TIER =
            new RegistryBuilder<>(ENERGY_TIER_KEY).create();
}
