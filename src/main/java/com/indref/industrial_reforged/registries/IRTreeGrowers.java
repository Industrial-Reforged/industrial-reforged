package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.datagen.IRWorldGenProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public final class IRTreeGrowers {

    private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        return IRWorldGenProvider.RUBBER_TREE_KEY;
    }
}
