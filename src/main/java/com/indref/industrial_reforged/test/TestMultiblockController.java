package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.api.blocks.IWrenchable;
import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class TestMultiblockController extends Block implements IMultiBlockController, IWrenchable {
    public TestMultiblockController(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TestMultiblock.TEST_PART);
    }

    @Override
    public IMultiblock getMultiblock() {
        return new TestMultiblock();
    }
}
