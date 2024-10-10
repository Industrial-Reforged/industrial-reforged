package com.indref.industrial_reforged.content.blockentities.misc;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FaucetBlockEntity extends ContainerBlockEntity {
    private BlockCapabilityCache<IFluidHandler, Direction> drainCapCache;
    private BlockCapabilityCache<IFluidHandler, Direction> fillCapCache;

    public FaucetBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.FAUCET.get(), p_155229_, p_155230_);
        addFluidTank(10);
    }

    @Override
    public void onLoad() {
        if (level instanceof ServerLevel serverLevel) {
            Direction dir = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
            IndustrialReforged.LOGGER.debug("Creating cap cache, at pos: {}", getBlockPos().relative(dir));
            this.drainCapCache = BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, serverLevel, getBlockPos().relative(dir), dir);
            this.fillCapCache = BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, serverLevel, getBlockPos().below(), Direction.DOWN);
            IndustrialReforged.LOGGER.debug("[load] cache Cap: {}, actual cap: {}", drainCapCache.getCapability(),
                    level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().relative(dir), dir));
            IndustrialReforged.LOGGER.debug("TET");
        }
        super.onLoad();
    }


    @Override
    public void commonTick() {
        if (!level.isClientSide()) {
            super.commonTick();
            Direction dir = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
            IFluidHandler thisHandler = getFluidHandler();
            IFluidHandler drainHandler = drainCapCache.getCapability();
            IFluidHandler fillHandler = fillCapCache.getCapability();
            if (drainHandler != null) {
                FluidStack drained = drainHandler.drain(Math.min(thisHandler.getTankCapacity(0), drainHandler.getFluidInTank(0).getAmount()), IFluidHandler.FluidAction.EXECUTE);
                int filled = thisHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOActions, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        return ImmutableMap.of();
    }
}
