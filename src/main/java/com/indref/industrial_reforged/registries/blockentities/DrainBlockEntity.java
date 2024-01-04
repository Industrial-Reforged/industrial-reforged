package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DrainBlockEntity extends BlockEntity {
    public DrainBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.DRAIN.get(), p_155229_, p_155230_);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
    }
}
