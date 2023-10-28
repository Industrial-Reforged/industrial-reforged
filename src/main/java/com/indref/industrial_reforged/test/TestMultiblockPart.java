package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.api.multiblocks.IMultiBlockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class TestMultiblockPart extends Block implements IMultiBlockPart {
    public TestMultiblockPart(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TestMultiblock.TEST_PART);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        level.setBlockAndUpdate(blockPos.offset(0, -1, 0), blockState.setValue(TestMultiblock.TEST_PART, TestMultiblock.PartIndex.getPartIndexByIndices(0, 0)));
        return InteractionResult.SUCCESS;
    }
}
