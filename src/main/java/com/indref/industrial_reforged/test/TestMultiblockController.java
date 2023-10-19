package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.api.blocks.IWrenchable;
import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import net.minecraft.world.level.block.Block;

public class TestMultiblockController extends Block implements IMultiBlockController, IWrenchable {
    public TestMultiblockController(Properties properties) {
        super(properties);
    }

    @Override
    public IMultiblock getMultiblock() {
        return new TestMultiblock();
    }
}
