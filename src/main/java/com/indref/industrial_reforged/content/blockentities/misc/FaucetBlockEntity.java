package com.indref.industrial_reforged.content.blockentities.misc;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.container.IRContainerBlockEntity;
import com.indref.industrial_reforged.content.blockentities.CastingBasinBlockEntity;
import com.indref.industrial_reforged.networking.BasinFluidChangedPayload;
import com.indref.industrial_reforged.networking.FaucetSetRenderStack;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FaucetBlockEntity extends IRContainerBlockEntity {
    private BlockCapabilityCache<IFluidHandler, Direction> drainCapCache;
    private BlockCapabilityCache<IFluidHandler, Direction> fillCapCache;
    private FluidStack renderStack = FluidStack.EMPTY;

    public FaucetBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.FAUCET.get(), p_155229_, p_155230_);
    }

    @Override
    public void onLoad() {
        if (level instanceof ServerLevel serverLevel) {
            Direction dir = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
            this.drainCapCache = BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, serverLevel, getBlockPos().relative(dir), dir);
            this.fillCapCache = BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, serverLevel, getBasinPos(), Direction.UP);
        }
        super.onLoad();
    }

    private @NotNull BlockPos getBasinPos() {
        return getBlockPos().below();
    }

    public FluidStack getRenderStack() {
        return renderStack;
    }

    public void setRenderStack(FluidStack renderStack) {
        this.renderStack = renderStack;
    }

    @Override
    public void commonTick() {
        super.commonTick();

        if (level instanceof ServerLevel serverLevel) {
            ChunkPos pos = new ChunkPos(worldPosition);
            IFluidHandler drainHandler = drainCapCache.getCapability();
            IFluidHandler fillHandler = fillCapCache.getCapability();
            if (drainHandler != null
                    && fillHandler != null
                    && level.getBlockEntity(getBasinPos()) instanceof CastingBasinBlockEntity be
                    && be.hasMold()
                    && be.getFluidTank().getFluidAmount() < be.getFluidTank().getCapacity()) {
                FluidStack drained = drainHandler.drain(2, IFluidHandler.FluidAction.EXECUTE);
                int filled = fillHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                int diff = drained.getAmount() - filled;
                drainHandler.fill(drained.copyWithAmount(diff), IFluidHandler.FluidAction.EXECUTE);
                this.renderStack = drained;
                PacketDistributor.sendToPlayersTrackingChunk(
                        serverLevel,
                        pos,
                        new BasinFluidChangedPayload(getBasinPos(), fillHandler.getFluidInTank(0).getAmount())
                );
                PacketDistributor.sendToPlayersTrackingChunk(
                        serverLevel,
                        pos,
                        new FaucetSetRenderStack(worldPosition, fillHandler.getFluidInTank(0))
                );
            } else if (!this.renderStack.isEmpty()) {
                this.renderStack = FluidStack.EMPTY;
                PacketDistributor.sendToPlayersTrackingChunk(
                        serverLevel,
                        pos,
                        new FaucetSetRenderStack(worldPosition, FluidStack.EMPTY)
                );
            }
        }
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        return ImmutableMap.of();
    }
}
