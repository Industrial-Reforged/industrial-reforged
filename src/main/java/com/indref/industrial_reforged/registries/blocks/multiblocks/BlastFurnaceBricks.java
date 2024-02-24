package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlastFurnaceBricks extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlastFurnaceBricks(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlastFurnaceMultiblock.BRICK_STATE, FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return super.getStateForPlacement(p_49820_)
                .setValue(FACING, Direction.NORTH)
                .setValue(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.UNFORMED);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult p_60508_) {
        if (player.isShiftKeyDown() && blockState.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.UNFORMED)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.TOP));
        } else {
            level.setBlockAndUpdate(blockPos, blockState.setValue(FACING, incDirection(blockState.getValue(FACING))));
        }
        return InteractionResult.SUCCESS;
    }

    private static Direction incDirection(Direction facing) {
        int facingIndex = Utils.facingToIndex(facing);
        facingIndex++;
        if (facingIndex > 3)
            return Direction.NORTH;
        return Utils.indexToFacing(facingIndex);
    }
}
