package com.indref.industrial_reforged.datagen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.IRBlocks;
import com.indref.industrial_reforged.worldgen.RubberTreeFoliagePlacer;
import com.indref.industrial_reforged.worldgen.RubberTreeTrunkPlacer;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class IRWorldGenProvider extends DatapackBuiltinEntriesProvider {
	public static final ResourceKey<ConfiguredFeature<?, ?>> RUBBER_TREE_KEY = registerConfigKey("rubber_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> URANIUM_ORE_KEY = registerConfigKey("uranium_ore");
	public static final ResourceKey<PlacedFeature> RUBBER_TREE_PLACE_KEY = registerPlaceKey("rubber_tree");
	public static final ResourceKey<PlacedFeature> URANIUM_ORE_PLACE_KEY = registerPlaceKey("uranium_ore");
    private static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    private static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

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
				new RubberTreeTrunkPlacer(2, 3, 1),
				BlockStateProvider.simple(IRBlocks.RUBBER_TREE_LEAVES.get()),
				new RubberTreeFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1)),
				new TwoLayersFeatureSize(2, 0, 2)).build()));
			List<OreConfiguration.TargetBlockState> uranium_config = List.of(
				OreConfiguration.target(STONE_ORE_REPLACEABLES, IRBlocks.RUBBER_TREE_BUTTON.get().defaultBlockState()),
				OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, IRBlocks.RUBBER_TREE_BUTTON.get().defaultBlockState()));
			context.register(URANIUM_ORE_KEY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(uranium_config, 4)));
		})
		.add(Registries.PLACED_FEATURE, context -> {
			HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
			context.register(RUBBER_TREE_PLACE_KEY, new PlacedFeature(configuredFeatures.getOrThrow(RUBBER_TREE_KEY),
				VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2), IRBlocks.RUBBER_TREE_SAPLING.get())));
			context.register(URANIUM_ORE_PLACE_KEY, new PlacedFeature(configuredFeatures.getOrThrow(URANIUM_ORE_KEY),
				List.of(HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-64), VerticalAnchor.absolute(80)), InSquarePlacement.spread(), BiomeFilter.biome())));
		});
}