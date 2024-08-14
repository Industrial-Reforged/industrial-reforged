package com.indref.industrial_reforged.api.multiblocks;

import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.IntegerRange;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Multiblock {
    // Returns the block that is the controller
    Block getController();

    /**
     * Return the layout of the multi
     * The order of this is bottom layer first.
     * As an example see: {@link CrucibleMultiblock#getLayout()}
     *
     * <br>[<br>0, 0, 0,
     * <br>0, 1, 0
     * <br>0, 0, 0<br>]
     */
    MultiblockLayer[] getLayout();

    /**
     * Use this method to assign the numbers from getLayout() to a block
     * <br> <br>
     * Use null if you don't want to check a block
     */
    Int2ObjectMap<Block> getDefinition();

    /**
     * IMPORTANT: override this if your
     * multi-block's layers are not quadratic
     * <p>
     * The list this returns needs to have a size of
     * {@link Multiblock#getMaxSize()} and needs to
     * contain the widths for all possible layers
     *
     * @return left is x and right is z width
     */
    default List<IntIntPair> getWidths() {
        List<IntIntPair> widths = new ArrayList<>(getMaxSize());
        for (MultiblockLayer layer : getLayout()) {
            if (layer.dynamic()) {
                for (int i = 0; i < layer.range().getMaximum(); i++) {
                    widths.add(layer.getWidths());
                }
            } else {
                widths.add(layer.getWidths());
            }
        }
        return widths;
    }

    Optional<BlockState> formBlock(Level level, HorizontalDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY, boolean dynamic, @Nullable Player player);

    /**
     * This gets called after the block at `blockpos` is formed
     * @param direction Direction of the multiblock
     * @param blockPos BlockPos of the block that was unformed
     * @param controllerPos BlockPos of the controller block of this multi
     * @param indexX Current multiblock index on the x-axis
     * @param indexY Current multiblock index on the y-axis
     */
    default void afterFormBlock(Level level, HorizontalDirection direction, BlockPos blockPos, BlockPos controllerPos, int indexX, int indexY, boolean dynamic) {
    }

    /**
     * This gets called after the block at `blockpos` is unformed
     * @param direction Direction of the multiblock
     * @param blockPos BlockPos of the block that was unformed
     * @param controllerPos BlockPos of the controller block of this multi
     * @param indexX Current multiblock index on the x-axis
     * @param indexY Current multiblock index on the y-axis
     */
    default void afterUnformBlock(Level level, HorizontalDirection direction, BlockPos blockPos, BlockPos controllerPos, int indexX, int indexY) {
    }

    boolean isFormed(Level level, BlockPos blockPos, BlockPos controllerPos);

    default Optional<HorizontalDirection> getFixedDirection() {
        return Optional.empty();
    }

    default MultiblockLayer layer(int... layer) {
        return new MultiblockLayer(false, IntegerRange.of(1, 1), layer);
    }


    default MultiblockLayer dynLayer(IntegerRange minMaxSize, int ...layer) {
        return new MultiblockLayer(true, minMaxSize, layer);
    }

    default int getMaxSize() {
        int maxSize = 0;
        for (MultiblockLayer layer : getLayout()) {
            maxSize += layer.range().getMaximum();
        }
        return maxSize;
    }
}
