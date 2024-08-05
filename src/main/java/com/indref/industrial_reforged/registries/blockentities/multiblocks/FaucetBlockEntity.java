package com.indref.industrial_reforged.registries.blockentities.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.CanAttachFaucetBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.indref.industrial_reforged.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

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
}
