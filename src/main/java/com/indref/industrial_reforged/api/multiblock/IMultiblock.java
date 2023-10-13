package com.indref.industrial_reforged.api.multiblock;

import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Interface for creating multiblocks
 */
public interface IMultiblock {
    /**
     * Used to define layout of the multi.
     * Example:
     * [
     * [0,0, // first list is the lowest level
     * 0,0],
     * [0, 0,
     * 0, 0]
     * ]
     * <br>
     * <br>
     * This defines a 2x2x2 multiblock that only
     * consists of a single block type (1)
     */
    List<List<Integer>> getLayout();

    /**
     * Used for defining blocks in the layout.
     * Take the previous example from getLayout()
     * <br>
     * <br>
     * There we use 1 in the layout.
     * Here we can define 1 as a Block that needs
     * to be used in the multiblock for every 1.
     */
    Map<Integer, Block> getDefinition();

    Block getController();

    // Override this if your multi's layers are not quadratic
    default List<Integer> getWidths() {
        List<Integer> widths = new ArrayList<>();
        for (List<Integer> list : getLayout()) {
            widths.add((int) Math.sqrt(list.size()));
        }
        return widths;
    }
}
