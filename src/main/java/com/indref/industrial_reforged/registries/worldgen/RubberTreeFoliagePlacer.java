package com.indref.industrial_reforged.registries.worldgen;

import com.indref.industrial_reforged.registries.IRPlacerTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class RubberTreeFoliagePlacer extends FoliagePlacer {
	public static final MapCodec<RubberTreeFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
		foliagePlacerParts(instance).apply(instance, RubberTreeFoliagePlacer::new));

	public RubberTreeFoliagePlacer(IntProvider radius, IntProvider offset) {
		super(radius, offset);
	}

	@Override
	protected FoliagePlacerType<?> type() {
		return IRPlacerTypes.RUBBER_TREE_FOLIAGE_PLACER.get();
	}

	@Override
	protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliagePlacer.FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
		placeLeavesRow(level, foliageSetter, random, config, attachment.pos(), 0, offset + 2, attachment.doubleTrunk());
		placeLeavesRow(level, foliageSetter, random, config, attachment.pos(), 0, offset + 1, attachment.doubleTrunk());
		placeLeavesRow(level, foliageSetter, random, config, attachment.pos(), 1, offset, attachment.doubleTrunk());
		placeLeavesRow(level, foliageSetter, random, config, attachment.pos(), 1, offset - 1, attachment.doubleTrunk());
		placeLeavesRow(level, foliageSetter, random, config, attachment.pos(), 2, offset - 2, attachment.doubleTrunk());
		placeLeavesRow(level, foliageSetter, random, config, attachment.pos(), 2, offset - 3, attachment.doubleTrunk());
	}

	public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
		return height;
	}

	protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
		if (range == localX && range == localZ & range > 0)
			return random.nextBoolean();
		return false;
	}
}