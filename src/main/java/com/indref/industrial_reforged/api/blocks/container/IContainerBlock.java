package com.indref.industrial_reforged.api.blocks.container;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IContainerBlock {
    void setStored(BlockEntity blockEntity, int value);
    int getStored(BlockEntity blockEntity);
    int getCapacity(BlockEntity blockEntity);
}
