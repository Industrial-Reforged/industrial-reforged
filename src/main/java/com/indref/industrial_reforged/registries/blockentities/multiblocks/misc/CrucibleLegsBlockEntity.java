package com.indref.industrial_reforged.registries.blockentities.multiblocks.misc;

import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CrucibleLegsBlockEntity extends BlockEntity {
    public CrucibleLegsBlockEntity(BlockPos pos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE_LEGS.get(), pos, blockState);
    }
}
