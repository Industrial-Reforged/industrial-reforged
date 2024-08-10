package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
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

    @Override
    public void commonTick() {
        super.commonTick();
        FluidState fluidOnTop = level.getFluidState(worldPosition.above());
        if (fluidOnTop.is(FluidTags.WATER) && fluidOnTop.isSource()) {
            if (level.getGameTime() % 40 == 0) {
                IFluidHandler fluidTank = CapabilityUtils.fluidHandlerCapability(this);
                if (fluidTank.getFluidInTank(0).getAmount() < fluidTank.getTankCapacity(0)) {
                    level.setBlockAndUpdate(worldPosition.above(), Blocks.AIR.defaultBlockState());
                    fluidTank.fill(new FluidStack(fluidOnTop.getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }
}
