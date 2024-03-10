package com.indref.industrial_reforged.api.multiblocks;

import net.minecraft.core.BlockPos;

/**
 * This interface allows your blockentity to set a controller pos
 */
public interface SavesControllerPos {
    void setControllerPos(BlockPos blockPos);
}
