package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.Map;

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

    @Override
    public Map<Direction, Pair<IOActions, int[]>> getItemIO() {
        return Map.of();
    }

    @Override
    public Map<Direction, Pair<IOActions, int[]>> getFluidIO() {
        return Map.of(
                Direction.NORTH, Pair.of(IOActions.EXTRACT, new int[]{0}),
                Direction.EAST, Pair.of(IOActions.EXTRACT, new int[]{0}),
                Direction.SOUTH, Pair.of(IOActions.EXTRACT, new int[]{0}),
                Direction.WEST, Pair.of(IOActions.EXTRACT, new int[]{0}),
                Direction.DOWN, Pair.of(IOActions.EXTRACT, new int[]{0})
        );
    }
}
