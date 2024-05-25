package com.indref.industrial_reforged.registries.blockentities.machines;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class DrainBlockEntity extends ContainerBlockEntity {
    public DrainBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.DRAIN.get(), p_155229_, p_155230_);
        addFluidTank(16000);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        FluidState fluidOnTop = level.getFluidState(blockPos.above());
        if (fluidOnTop.is(FluidTags.WATER) && fluidOnTop.isSource()) {
            if (level.getGameTime() % 40 == 0) {
                getFluidTank().ifPresent(fluidTank -> {
                    if (fluidTank.getFluidInTank(0).getAmount() < fluidTank.getTankCapacity(0)) {
                        level.setBlockAndUpdate(blockPos.above(), Blocks.AIR.defaultBlockState());
                        fluidTank.fill(new FluidStack(fluidOnTop.getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
                    }
                });
            }
        }
    }
}
