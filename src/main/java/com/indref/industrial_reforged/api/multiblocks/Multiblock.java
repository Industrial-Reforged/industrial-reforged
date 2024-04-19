package com.indref.industrial_reforged.api.multiblocks;

import com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// TODO: 10/23/2023 Allow rotating the multi (currently only works for south)
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
    List<List<Integer>> getLayout();

    /**
     * Use this method to assign the numbers from getLayout() to a block
     * <br> <br>
     * Use null if you don't want to check a block
     */
    Map<Integer, @Nullable Block> getDefinition();

    /**
     * !!! IMPORTANT: override this if your
     * multi-block's layers are not quadratic
     * <br>
     * First Integer is the x direction ([0, 0, 0...)
     * Second Integer is the z direction
     * <br>[<br>0,...
     * <br>                                0,...
     * <br>                                0,...
     * <br>]
     */
    default List<Pair<Integer, Integer>> getWidths() {
        List<Pair<Integer, Integer>> widths = new ArrayList<>();
        for (List<Integer> list : getLayout()) {
            int width = (int) Math.sqrt(list.size());
            widths.add(Pair.of(width, width));
        }
        return widths;
    }

    Optional<BlockState> formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY);

    /**
     * This gets called after the block at `blockpos` is formed
     * @param direction Direction of the multiblock
     * @param blockPos BlockPos of the block that was unformed
     * @param controllerPos BlockPos of the controller block of this multi
     * @param indexX Current multiblock index on the x-axis
     * @param indexY Current multiblock index on the y-axis
     */
    default void afterFormBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int indexX, int indexY) {
    }

    /**
     * This gets called after the block at `blockpos` is unformed
     * @param direction Direction of the multiblock
     * @param blockPos BlockPos of the block that was unformed
     * @param controllerPos BlockPos of the controller block of this multi
     * @param indexX Current multiblock index on the x-axis
     * @param indexY Current multiblock index on the y-axis
     */
    default void afterUnformBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int indexX, int indexY) {
    }

    boolean isFormed(Level level, BlockPos blockPos, BlockPos controllerPos);

    default Optional<MultiblockDirection> getFixedDirection() {
        return Optional.empty();
    }
}
