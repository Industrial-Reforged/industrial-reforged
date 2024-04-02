package com.indref.industrial_reforged.api.tiers;

import net.minecraft.world.level.block.Block;

public interface CrucibleTier {
    int getHeatCapacity();
    Block getController();
    Block getCrucibleWallBlock();
}
