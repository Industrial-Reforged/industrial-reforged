package com.indref.industrial_reforged.registries.blockentities.misc;

import com.indref.industrial_reforged.api.blocks.misc.CanAttachFaucetBlock;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FaucetBlockEntity extends ContainerBlockEntity {
    public FaucetBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.FAUCET.get(), p_155229_, p_155230_);
        addFluidTank(10);
    }

    @Override
    public void commonTick() {
        super.commonTick();
        Direction facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        BlockPos fromFluidHandlerPos = worldPosition.relative(facing);
        BlockEntity blockEntity = level.getBlockEntity(fromFluidHandlerPos);
        if (blockEntity != null) {
            if (blockEntity.getBlockState().getBlock() instanceof CanAttachFaucetBlock) {
            }
        }
    }

    @Override
    public <T> Map<Direction, Pair<IOActions, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        return Map.of();
    }
}
