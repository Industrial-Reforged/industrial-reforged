package com.indref.industrial_reforged.content.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.IRBlocks;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

public class IRWorldGenProvider extends DatapackBuiltinEntriesProvider {
	public static final ResourceKey<ConfiguredFeature<?, ?>> RUBBER_TREE_KEY = registerConfigKey("rubber_tree");
	public static final ResourceKey<PlacedFeature> RUBBER_TREE_PLACE_KEY = registerPlaceKey("rubber_tree");

	public IRWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of(IndustrialReforged.MODID));
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> registerConfigKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(IndustrialReforged.MODID, name));
	}

	public static ResourceKey<PlacedFeature> registerPlaceKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(IndustrialReforged.MODID, name));
	}

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
		.add(Registries.CONFIGURED_FEATURE, context -> {
			context.register(RUBBER_TREE_KEY, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(IRBlocks.RUBBER_TREE_LOG.get()),
				new StraightTrunkPlacer(5, 6, 3),
				BlockStateProvider.simple(IRBlocks.RUBBER_TREE_LEAVES.get()),
				new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 4),
				new TwoLayersFeatureSize(1, 0, 2)).build()));
		})
		.add(Registries.PLACED_FEATURE, context -> {
			HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
			context.register(RUBBER_TREE_PLACE_KEY, new PlacedFeature(configuredFeatures.getOrThrow(RUBBER_TREE_KEY),
				VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2), IRBlocks.RUBBER_TREE_SAPLING.get())));
		});
}