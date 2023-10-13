package com.indref.industrial_reforged.content.multiblocks;

import com.indref.industrial_reforged.api.multiblock.IMultiblock;
import com.indref.industrial_reforged.content.IRBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMultiblock implements IMultiblock {
    @Override
    public List<List<Integer>> getLayout() {
        return List.of(
                List.of(
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, 0
                ),
                List.of(
                        0, 0, 0,
                        0, 1, 0,
                        0, 0, 0
                )
        );
    }

    @Override
    public Map<Integer, Block> getDefinition() {
        Map<Integer, Block> def = new HashMap<>();
        def.put(0, Blocks.BARRIER);
        def.put(1, IRBlocks.TEST_CONTROLLER.get());
        return def;
    }

    @Override
    public Block getController() {
        return IRBlocks.TEST_CONTROLLER.get();
    }
}
