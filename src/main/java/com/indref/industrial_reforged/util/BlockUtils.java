package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Executable;

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

    public static <T, C> T getBlockEntityCapability(BlockCapability<T, C> cap, BlockEntity blockEntity) {
        return getBlockEntityCapability(cap, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null, blockEntity.getLevel());
    }

    private static <T, C> T getBlockEntityCapability(BlockCapability<T, C> cap, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity, C context, Level level) {
        return level.getCapability(cap, pos, state, blockEntity, context);
    }

    public static @Nullable IItemHandler getClientItemHandler(BlockPos blockPos) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity != null) {
                return BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
            }
        }
        return null;
    }

    public static @Nullable IHeatBlock getHeatBlock(BlockEntity blockEntity) {
        if (BlockUtils.isHeatBlock(blockEntity))
            return (IHeatBlock) blockEntity;
        return null;
    }

    public static boolean isEnergyBlock(BlockEntity blockEntity) {
        return blockEntity != null && getBlockEntityCapability(IRCapabilities.EnergyStorage.BLOCK, blockEntity) != null;
    }

    public static boolean isHeatBlock(BlockEntity blockEntity) {
        return blockEntity != null && getBlockEntityCapability(IRCapabilities.HeatStorage.BLOCK, blockEntity) != null;
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
