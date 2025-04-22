package com.indref.industrial_reforged.api.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Implement this if you want your block to drop a special
 * item when picked up with a wrench. Still requires the
 * block to have the {@link com.indref.industrial_reforged.tags.IRTags.Blocks.WRENCHABLE}
 */
public interface CustomWrenchableBlock {
    ItemStack getCustomDropItem();

    default boolean canWrench(Level level, BlockPos blockPos, BlockState blockState) {
        return true;
    }
}
