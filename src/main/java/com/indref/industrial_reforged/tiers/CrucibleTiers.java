package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum CrucibleTiers implements CrucibleTier {
    CERAMIC("ceramic", 1200,
            IRBlocks.TERRACOTTA_BRICK_SLAB::get,
            IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER::get,
            IRBlocks.TERRACOTTA_BRICK,
            IRBlocks.CERAMIC_CRUCIBLE_PART::get
    );

    private final int heatCapacity;
    private final String name;
    private final Supplier<Block> unformedController;
    private final Supplier<Block> formedController;
    private final Supplier<Block> unformedPart;
    private final Supplier<Block> formedPart;

    CrucibleTiers(String name, int heatCapacity, Supplier<Block> unformedController, Supplier<Block> formedController, Supplier<Block> unformedPart, Supplier<Block> formedPart) {
        this.name = name;
        this.heatCapacity = heatCapacity;
        this.unformedController = unformedController;
        this.formedController = formedController;
        this.unformedPart = unformedPart;
        this.formedPart = formedPart;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHeatCapacity() {
        return this.heatCapacity;
    }

    @Override
    public Block getUnformedController() {
        return this.unformedController.get();
    }

    @Override
    public Block getFormedController() {
        return this.formedController.get();
    }

    @Override
    public Block getUnformedPart() {
        return this.unformedPart.get();
    }

    @Override
    public Block getFormedPart() {
        return this.formedPart.get();
    }
}
