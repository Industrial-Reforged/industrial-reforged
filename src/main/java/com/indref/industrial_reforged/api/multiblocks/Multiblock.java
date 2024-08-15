package com.indref.industrial_reforged.api.multiblocks;

import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.util.MultiblockHelper;
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
    /**
     * This method provides the controller block of your unformed multiblock.
     * Your multiblock needs at least one of these in its structure.
     * <br>
     * <br>
     * Example: {@link com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock#getController() BlastFurnaceMultiblock.getController()}
     * <br>
     * @return The controller block of your unformed multiblock
     */
    Block getController();

    /**
     * This method provides the layout of your unformed multiblock.
     * <br>
     * It consists of an array of multiblock layers. Each layer
     * is constructed with a method call.
     * <br>
     * For this, you will use
     * {@link Multiblock#layer(int...)} or {@link Multiblock#dynLayer(IntegerRange, int...)}
     * for dynamically sized layers.
     * <br>
     * Each of these methods ask you to provide you a list of integers.
     * These integers represent the actual blocks used.
     * Nonetheless, you still need to provide the actual blocks using
     * the {@link Multiblock#getDefinition()} method.
     * In the {@link Multiblock#dynLayer(IntegerRange, int...)} method you also need to provide an integer range.
     * This provides the minimum and maximum height for this multiblock.
     * <br>
     * Example: {@code IntegerRange.of(1, 3)}
     * <br>
     * <br>
     * Note: The first layer in this array also represents the bottom layer of the multiblock
     * <br>
     * <br>
     * Example: {@link com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock#getLayout() BlastFurnaceMultiblock.getLayout()}.
     * @return An array of multiblock layers that describes the layout of the multiblock
     */
    MultiblockLayer[] getLayout();

    /**
     * This method provides a definition map that can be used to look up
     * an integer key in {@link Multiblock#getLayout()} and will return a block.
     * <br>
     * <br>
     * The keyset of this map needs to include
     * every key that is used in {@link Multiblock#getLayout()}.
     * <br>
     * <br>
     * The values of this map need to contain the block for each
     * integer key. If you do not care about a block you can use {@code null}
     * instead of a value.
     * <br>
     * <br>
     * Example: {@link com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock#getDefinition() BlastFurnaceMultiblock.getDefintion()}
     * @return The integer to block map that provides the integer keys and their block values
     */
    Int2ObjectMap<Block> getDefinition();

    /**
     * This method provides a list of widths for every layer
     * of your multiblock.
     * <br>
     * <br>
     * This method has a default implementation meaning that
     * you do not have to override it, unless one of your
     * multiblock layers is not quadratic. (And it's width
     * can therefore not be determined by getting the
     * square root of the integer arrays length)
     * <br>
     * <br>
     * The size of this list needs to be {@link Multiblock#getMaxSize()}
     * and needs to contain the widths for every possible layer, this also
     * includes dynamic layers.
     *
     * @return a list of integer pairs where left is the x- and right is the z-width
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

    /**
     *
     * @param level
     * @param blockPos
     * @param controllerPos
     * @param xIndex
     * @param yIndex
     * @param unformedMultiblock
     * @param player
     * @return
     */
    @Nullable BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int xIndex, int yIndex, MultiblockHelper.UnformedMultiblock unformedMultiblock, @Nullable Player player);

    /**
     * This gets called after the block at `blockpos` is formed
     * @param direction Direction of the multiblock
     * @param blockPos BlockPos of the block that was unformed
     * @param controllerPos BlockPos of the controller block of this multi
     * @param indexX Current multiblock index on the x-axis
     * @param indexY Current multiblock index on the y-axis
     */
    default void afterFormBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int xIndex, int yIndex, MultiblockHelper.UnformedMultiblock unformedMultiblock, @Nullable Player player) {
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

    default @Nullable HorizontalDirection getFixedDirection() {
        return null;
    }

    default int getMaxSize() {
        int maxSize = 0;
        for (MultiblockLayer layer : getLayout()) {
            maxSize += layer.range().getMaximum();
        }
        return maxSize;
    }

    default MultiblockLayer layer(int... layer) {
        return new MultiblockLayer(false, IntegerRange.of(1, 1), layer);
    }


    default MultiblockLayer dynLayer(IntegerRange minMaxSize, int ...layer) {
        return new MultiblockLayer(true, minMaxSize, layer);
    }
}
