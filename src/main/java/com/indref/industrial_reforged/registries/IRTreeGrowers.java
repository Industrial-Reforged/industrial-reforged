package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.datagen.data.IRWorldGenProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class IRTreeGrowers {
    public static final TreeGrower RUBBER = new TreeGrower("rubber", Optional.empty(), Optional.of(IRWorldGenProvider.RUBBER_TREE_KEY), Optional.empty());

    private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        return IRWorldGenProvider.RUBBER_TREE_KEY;
    }
}
