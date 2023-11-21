package com.indref.industrial_reforged.api.tiers.templates;

import net.minecraft.world.level.block.Block;

public interface CrucibleTier {
    int heatCapacity();
    Block getController();
    Block getCrucibleWallBlock();
}
