package com.indref.industrial_reforged.content.blockentities.multiblocks.part;

import com.indref.industrial_reforged.api.blockentities.multiblock.MultiblockPartBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnacePartBlockEntity extends MultiblockPartBlockEntity {
    public BlastFurnacePartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.BLAST_FURNACE_PART.get(), blockPos, blockState);
    }
}
