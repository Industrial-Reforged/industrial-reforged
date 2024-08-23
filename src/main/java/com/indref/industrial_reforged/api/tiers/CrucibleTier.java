package com.indref.industrial_reforged.api.tiers;

import net.minecraft.world.level.block.Block;

public interface CrucibleTier {
    String getName();

    int getHeatCapacity();

    Block getUnformedController();

    Block getFormedController();

    Block getUnformedPart();

    Block getFormedPart();
}
