package com.indref.industrial_reforged.worldgen;

import javax.annotation.Nullable;

import com.indref.industrial_reforged.content.datagen.IRWorldGenProvider;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class RubberTreeGrower extends AbstractTreeGrower {

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_222910_, boolean p_222911_) {
        return IRWorldGenProvider.RUBBER_TREE_KEY;
    }
}
