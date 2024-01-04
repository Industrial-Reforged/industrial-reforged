package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CentrifugeBlockEntity extends BlockEntity {
    public CentrifugeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CENTRIFUGE.get(), p_155229_, p_155230_);
    }
}
