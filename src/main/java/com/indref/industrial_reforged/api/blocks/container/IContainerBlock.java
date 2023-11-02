package com.indref.industrial_reforged.api.blocks.container;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IContainerBlock {
    /**
     * Call this method in the setStored() method to indicate changes
     */
    default void onChanged() {
    }

    void setStored(BlockEntity blockEntity, int value);

    int getStored(BlockEntity blockEntity);

    int getCapacity(BlockEntity blockEntity);

    boolean tryDrain(BlockEntity blockEntity, int amount);

    boolean tryFill(BlockEntity blockEntity, int amount);
}
