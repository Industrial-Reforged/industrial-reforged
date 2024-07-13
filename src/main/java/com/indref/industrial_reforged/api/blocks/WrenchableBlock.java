package com.indref.industrial_reforged.api.blocks;

import com.indref.industrial_reforged.registries.items.tools.WrenchItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * Implement this if you want your block to be able to be picked up by a
 * variant (inheritor) of the {@link WrenchItem} class
 */
public interface WrenchableBlock {
    /**
     * If you override this method it will use a custom drop.
     * If it returns null the block itself will drop
     * @return the item that should be dropped
     */
    default Optional<Item> getDropItem() {
        return Optional.empty();
    }

    default boolean canWrench(Level level, BlockPos blockPos, BlockState blockState) {
        return true;
    }
}
