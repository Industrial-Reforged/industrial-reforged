package com.indref.industrial_reforged.api.multiblocks;

import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IMultiblock {
    // TODO: 10/20/2023 why does this exist here? Should be moved to IMultiblockController 
    Block getController();
    List<List<Integer>> getLayout();
    Map<Integer, Block> getDefinition();
    default List<Integer> getWidths() {
        List<Integer> widths = new ArrayList<>();
        for (List<Integer> list : getLayout()) {
            widths.add((int) Math.sqrt(list.size()));
        }
        return widths;
    }
}
