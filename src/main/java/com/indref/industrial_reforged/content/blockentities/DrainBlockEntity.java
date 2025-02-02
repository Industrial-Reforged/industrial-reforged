package com.indref.industrial_reforged.content.blockentities;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.api.blockentities.container.IRContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class DrainBlockEntity extends IRContainerBlockEntity {
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
    public <T> ImmutableMap<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        if (capability == Capabilities.FluidHandler.BLOCK) {
            return ImmutableMap.of(
                    Direction.NORTH, Pair.of(IOAction.EXTRACT, new int[]{0}),
                    Direction.EAST, Pair.of(IOAction.EXTRACT, new int[]{0}),
                    Direction.SOUTH, Pair.of(IOAction.EXTRACT, new int[]{0}),
                    Direction.WEST, Pair.of(IOAction.EXTRACT, new int[]{0}),
                    Direction.DOWN, Pair.of(IOAction.EXTRACT, new int[]{0})
            );
        }
        return ImmutableMap.of();
    }
}
