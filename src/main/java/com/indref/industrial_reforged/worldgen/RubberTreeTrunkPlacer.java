package com.indref.industrial_reforged.worldgen;

import com.google.common.collect.ImmutableList;
import com.indref.industrial_reforged.content.IRBlocks;
import com.indref.industrial_reforged.content.blocks.trees.RubberTreeResinHoleBlock;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class RubberTreeTrunkPlacer extends TrunkPlacer {
	public static final Codec<RubberTreeTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> {
		return trunkPlacerParts(instance).apply(instance, RubberTreeTrunkPlacer::new);
	});

	public RubberTreeTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
		super(baseHeight, heightRandA, heightRandB);
	}

	protected TrunkPlacerType<?> type() {
		return IRPlacerTypes.RUBBER_TREE_TRUNK_PLACER.get();
	}

	public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int height, BlockPos pos, TreeConfiguration config) {
		setDirtAt(level, blockSetter, random, pos.below(), config);

		for (int i = 0; i < height + 1; i++) {
			if (random.nextInt(10) < 3 && i > 0 && i < height - 1) {
				BlockState state = IRBlocks.RUBBER_TREE_RESIN_HOLE.get().defaultBlockState();
				int j = random.nextInt(RubberTreeResinHoleBlock.FACING.getPossibleValues().size());
				Direction direction = (Direction) RubberTreeResinHoleBlock.FACING.getPossibleValues().toArray()[j];
				state = state.setValue(RubberTreeResinHoleBlock.FACING, direction).setValue(RubberTreeResinHoleBlock.RESIN, true);
				blockSetter.accept(pos.above(i), state);
			} else {
				placeLog(level, blockSetter, random, pos.above(i), config);
			}
		}

		return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pos.above(height), 0, false));
	}
}
