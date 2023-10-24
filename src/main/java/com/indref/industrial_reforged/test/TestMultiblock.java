package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.content.IRBlocks;
import net.minecraft.world.level.block.Block;

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
        // Giving the numbers from the layout a purpose
        return Map.of(
                0, IRBlocks.TEST_PART.get(),
                1, IRBlocks.TEST_CONTROLLER.get()
        );
    }
}
