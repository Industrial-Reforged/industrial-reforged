package com.indref.industrial_reforged.api.blocks.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

/**
 * Implement this interface on your block if you
 * want the molten metal of your faucet to go down further
 * than the max y
 */
public interface CustomFaucetInteractBlock {
    /**
     * @return the y controllerPos the molten metal should reach down to
     */
    float getShapeMaxY(BlockGetter blockGetter, BlockPos blockPos);
}
