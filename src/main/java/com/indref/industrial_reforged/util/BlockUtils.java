package com.indref.industrial_reforged.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public final class BlockUtils {
    public static BlockPos[] getBlocksAroundSelf(BlockPos selfPos) {
        return new BlockPos[] {
                selfPos.offset(1, 0, 0),
                selfPos.offset(0, 1, 0),
                selfPos.offset(0, 0, 1),
                selfPos.offset(-1, 0, 0),
                selfPos.offset(0, -1, 0),
                selfPos.offset(0, 0, -1)
        };
    }

    public static BlockPos[] getBlocksAroundSelf3x3(BlockPos selfPos) {
        return new BlockPos[] {
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

    public static <T, C> T getBlockEntityCapability(BlockCapability<T, C> cap, Level level, BlockEntity blockEntity) {
        return getBlockEntityCapability(cap, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null, level);
    }

    public static <T, C> T getBlockEntityCapability(BlockCapability<T, C> cap, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity, C context, Level level) {
        return level.getCapability(cap, pos, state, blockEntity, context);
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
