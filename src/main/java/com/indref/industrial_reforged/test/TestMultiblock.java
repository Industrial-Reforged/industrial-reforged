package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.content.IRBlocks;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMultiblock implements IMultiblock {
    @Override
    public Block getController() {
        return IRBlocks.TEST_CONTROLLER.get();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return List.of(
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
        def.put(0, IRBlocks.TEST_PART.get());
        def.put(1, IRBlocks.TEST_CONTROLLER.get());
        return def;
    }

}
