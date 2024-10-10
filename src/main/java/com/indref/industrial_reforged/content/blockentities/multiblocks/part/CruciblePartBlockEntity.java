package com.indref.industrial_reforged.registries.blockentities.multiblocks.part;

import com.indref.industrial_reforged.api.blockentities.multiblock.MultiblockPartBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CruciblePartBlockEntity extends MultiblockPartBlockEntity {
    public CruciblePartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE_PART.get(), blockPos, blockState);
    }
}
