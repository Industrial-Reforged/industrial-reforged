package com.indref.industrial_reforged.api.blockentities.multiblock;

import net.minecraft.core.BlockPos;

/**
 * This interface allows your blockentity to set a controller pos
 */
public interface SavesControllerPosBlockEntity {
    void setControllerPos(BlockPos blockPos);
}