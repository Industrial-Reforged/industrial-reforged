package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class IRWorldgenKeys {
    public static final Feature RUBBER_TREE_KEY = registerFeature("rubber_tree");
    public static final Feature BAUXITE_ORE_KEY = registerFeature("bauxite_ore");
    public static final Feature CHROMIUM_ORE_KEY = registerFeature("chromium_ore");
    public static final Feature IRIDIUM_ORE_KEY = registerFeature("iridium_ore");
    public static final Feature LEAD_ORE_KEY = registerFeature("lead_ore");
    public static final Feature NICKEL_ORE_KEY = registerFeature("nickel_ore");
    public static final Feature TIN_ORE_KEY = registerFeature("tin_ore");
    public static final Feature URANIUM_ORE_KEY = registerFeature("uranium_ore");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerConfigKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, name));
    }

    public static ResourceKey<PlacedFeature> registerPlaceKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, name));
    }

    public static Feature registerFeature(String name) {
        return new Feature(registerConfigKey(name), registerPlaceKey(name));
    }

    public record Feature(ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, ResourceKey<PlacedFeature> placedFeature) {
    }
}
