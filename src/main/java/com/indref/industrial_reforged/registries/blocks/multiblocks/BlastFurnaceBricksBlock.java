package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlastFurnaceBricksBlock extends Block implements WrenchableBlock {
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
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            Optional<BlockPos> controllerPos = Optional.empty();
            if (pState.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.TOP)) {
                controllerPos = Optional.of(pPos.offset(0, -3, 0));
            } else {
                for (int i = 1; i < 3; i++) {
                    BlockPos offsetPos = pPos.offset(0, -i, 0);
                    if (pLevel.getBlockEntity(offsetPos) instanceof BlastFurnaceBlockEntity) {
                        controllerPos = Optional.of(offsetPos);
                    }
                }
            }
            controllerPos.ifPresent(pos -> MultiblockHelper.unform(IRMultiblocks.BLAST_FURNACE.get(), pos, pLevel));
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
