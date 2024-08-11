package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.world.level.block.Block;

public enum CrucibleTiers implements CrucibleTier {
    CERAMIC("ceramic", 1200);

    private final int heatCapacity;
    private final String name;

    CrucibleTiers(String name, int heatCapacity) {
        this.name = name;
        this.heatCapacity = heatCapacity;
    }

    @Override
    public int getHeatCapacity() {
        return this.heatCapacity;
    }

    @Override
    public Block getController() {
        return IRBlocks.TERRACOTTA_BRICK_SLAB.get();
    }

    @Override
    public Block getCrucibleWallBlock() {
        return IRBlocks.TERRACOTTA_BRICK.get();
    }
}
