package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.util.MultiblockUtils;
import com.indref.industrial_reforged.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlastFurnaceBricksBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlastFurnaceBricksBlock(Properties properties) {
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
            level.setBlockAndUpdate(blockPos, blockState.setValue(FACING, Utils.incDirection(blockState.getValue(FACING))));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            BlockPos controllerPos = null;
            if (pState.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.TOP)) {
                controllerPos = pPos.offset(0, -3, 0);
            } else {
                for (int i = 1; i < 3; i++) {
                    BlockPos offsetPos = pPos.offset(0, -i, 0);
                    if (pLevel.getBlockEntity(offsetPos) instanceof BlastFurnaceBlockEntity) {
                        controllerPos = offsetPos;
                    }
                }
            }
            if (controllerPos != null) {
                MultiblockUtils.unform(IRMultiblocks.BLAST_FURNACE.get(), controllerPos, pLevel);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    /**
     * Get the blockpos of the main controler pos
     * @param blockPos blockpos of any block that is part of the blast furnace
     * @return BlockPos of the main controller block
     */
    public static BlockPos getControllerPos(Level level, BlockPos blockPos) {
        return null;
    }
}
