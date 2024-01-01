package com.indref.industrial_reforged.api.tiers;

import com.indref.industrial_reforged.api.tiers.templates.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public enum CrucibleTiers implements CrucibleTier {
    CERAMIC("ceramic", 1200);

    private final int heatCapacity;
    private final String name;

    CrucibleTiers(String name, int heatCapacity) {
        this.name = name;
        this.heatCapacity = heatCapacity;
    }

    @Override
    public int heatCapacity() {
        return this.heatCapacity;
    }

    @Override
    public Block getController() {
        return IRBlocks.TERRACOTTA_SLAB.get();
    }

    @Override
    public Block getCrucibleWallBlock() {
        return IRBlocks.TERRACOTTA_BRICK.get();
    }
}
