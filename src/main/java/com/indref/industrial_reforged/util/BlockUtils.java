package com.indref.industrial_reforged.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.List;

public final class BlockUtils {
    public static BlockPos[] getBlocksAroundSelf(BlockPos selfPos) {
        return new BlockPos[]{
                selfPos.offset(1, 0, 0),
                selfPos.offset(0, 1, 0),
                selfPos.offset(0, 0, 1),
                selfPos.offset(-1, 0, 0),
                selfPos.offset(0, -1, 0),
                selfPos.offset(0, 0, -1)
        };
    }

    public static BlockPos[] getBlocksAroundSelf3x3(BlockPos selfPos) {
        return new BlockPos[]{
                selfPos.offset(1, 0, 0),
                selfPos.offset(0, 0, 1),
                selfPos.offset(-1, 0, 0),
                selfPos.offset(0, 0, -1),
                selfPos.offset(1, 0, -1),
                selfPos.offset(-1, 0, 1),
                selfPos.offset(1, 0, 1),
                selfPos.offset(-1, 0, -1),
        };
    }

    public static BlockState rotateBlock(BlockState state, DirectionProperty prop, Comparable<?> currentValue) {
        List<Direction> directions = prop.getPossibleValues().stream().toList();
        int currentDirectionIndex = directions.indexOf(currentValue);
        int nextDirectionIndex = (currentDirectionIndex + 1) % directions.size();
        Direction nextDirection = directions.get(nextDirectionIndex);
        return state.setValue(prop, nextDirection);
    }

    public static BlockState stateForFacingPlacement(Block block, BlockPlaceContext ctx) {
        return block.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getPlayer().getDirection());
    }

    public static StateDefinition.Builder<Block, BlockState> createFacingDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        return builder;
    }

    /*
    public static EnergyNet.EnergyTypes getEnergyType(Level level, BlockPos blockPos) {
        if (level.getBlockState(blockPos).getBlock() instanceof CableBlock) {
            return EnergyNet.EnergyTypes.TRANSMITTERS;
        } else if (level.getBlockEntity(blockPos) instanceof GeneratorBlockEntity) {
            return EnergyNet.EnergyTypes.PRODUCERS;
        } else if (level.getBlockEntity(blockPos) instanceof IEnergyBlock && !(level.getBlockEntity(blockPos) instanceof GeneratorBlockEntity)) {
            return EnergyNet.EnergyTypes.CONSUMERS;
        }
        return null;
    }
     */
}
