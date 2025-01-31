package com.indref.industrial_reforged.registries;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public final class IRTreeGrowers {
    public static final TreeGrower RUBBER = new TreeGrower("rubber",
            Optional.empty(),
            Optional.of(IRWorldgenKeys.RUBBER_TREE_KEY.configuredFeature()),
            Optional.empty());
}
