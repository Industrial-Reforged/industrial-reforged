package com.indref.industrial_reforged.api.blocks.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface CanAttachFaucetBlock {
    boolean canAttachFaucetToBlock(BlockState blockState, BlockPos blockPos, Level level, Direction facing);
}
