package com.indref.industrial_reforged.content.blocks;

import com.indref.industrial_reforged.api.multiblock.IMultiblock;
import com.indref.industrial_reforged.api.multiblock.IMultiblockController;
import com.indref.industrial_reforged.content.multiblocks.TestMultiblock;
import net.minecraft.world.level.block.Block;

public class TestMultiblockControllerBlock extends Block implements IMultiblockController {
    public TestMultiblockControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public IMultiblock getMultiblock() {
        return new TestMultiblock();
    }
}
