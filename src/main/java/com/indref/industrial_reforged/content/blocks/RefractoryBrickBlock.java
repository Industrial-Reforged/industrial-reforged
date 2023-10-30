package com.indref.industrial_reforged.content.blocks;

import com.indref.industrial_reforged.api.multiblocks.IMultiBlockPart;
import com.indref.industrial_reforged.content.multiblocks.FireBoxMultiblock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class RefractoryBrickBlock extends Block implements IMultiBlockPart {
    public RefractoryBrickBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FireBoxMultiblock.FIREBOX_PART);
    }
}
