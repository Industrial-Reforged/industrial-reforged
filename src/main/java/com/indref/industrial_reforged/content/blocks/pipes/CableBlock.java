package com.indref.industrial_reforged.registries.blocks.pipes;

import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.transportation.energy.EnergyNet;
import com.indref.industrial_reforged.data.saved.EnergyNetsSavedData;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.indref.industrial_reforged.util.EnergyNetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CableBlock extends PipeBlock {
    private final EnergyTier energyTier;

    public CableBlock(Properties properties, int width, EnergyTier energyTier) {
        super(properties, width);
        this.energyTier = energyTier;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean p_60570_) {
        // perform this check to ensure that block is actually placed and not just block states updating
        if (oldState.is(Blocks.AIR) && level instanceof ServerLevel serverLevel) {
            EnergyNetsSavedData nets = EnergyNetUtils.getEnergyNets(serverLevel);
            // Adds the net
            EnergyNet net = nets.getEnets().getOrCreateNetAndPush(blockPos);
            nets.setDirty();
            for (BlockPos offsetPos : BlockUtils.getBlocksAroundSelf(blockPos)) {
                Block block = level.getBlockState(offsetPos).getBlock();
                if (block instanceof CableBlock) {
                    Optional<EnergyNet> network = nets.getEnets().getNetwork(offsetPos);
                    if (network.isPresent() && network.get() != net) {
                        nets.getEnets().mergeNets(net, network.get());
                        nets.setDirty();
                    }
                } else {
                    BlockEntity blockEntity = level.getBlockEntity(offsetPos);
                    if (blockEntity != null) {
                        IEnergyStorage energyStorage = CapabilityUtils.energyStorageCapability(blockEntity);
                        if (energyStorage != null) {
                            Optional<EnergyNet> network = nets.getEnets().getNetwork(blockPos);
                            network.ifPresent(energyNet -> energyNet.add(offsetPos, EnergyNet.EnergyTypes.INTERACTORS));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        super.onRemove(blockState, level, blockPos, newState, p_60519_);
        // perform this check to ensure that block was actually removed and not just block states updating
        if (!newState.is(blockState.getBlock()) && level instanceof ServerLevel serverLevel) {
            EnergyNetsSavedData nets = EnergyNetUtils.getEnergyNets(serverLevel);
            nets.getEnets().splitNets(blockPos);
            nets.getEnets().removeNetwork(blockPos);
            // Tell the level to save it
            nets.setDirty();
        }
    }

    @SuppressWarnings("OptionalIsPresent")
    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction facingDirection, BlockState facingBlockState, LevelAccessor level, BlockPos blockPos, BlockPos facingBlockPos) {
        if (level instanceof ServerLevel serverLevel) {
            Optional<EnergyNet> network = EnergyNetUtils.getEnergyNets(serverLevel).getEnets().getNetwork(blockPos);
            BlockEntity entity = level.getBlockEntity(facingBlockPos);
            if (entity != null && CapabilityUtils.energyStorageCapability(entity) != null) {
                if (network.isPresent()) {
                    network.get().add(facingBlockPos, EnergyNet.EnergyTypes.INTERACTORS);
                }
            } else if (network.isPresent() && network.get().get(EnergyNet.EnergyTypes.INTERACTORS).contains(facingBlockPos)) {
                if (facingBlockState.isEmpty()) {
                    network.get().remove(facingBlockPos, EnergyNet.EnergyTypes.INTERACTORS);
                }
            }
        }
        return super.updateShape(blockState, facingDirection, facingBlockState, level, blockPos, facingBlockPos);
    }

    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    @Override
    public boolean canConnectToPipe(BlockState connectTo) {
        return connectTo.getBlock() instanceof CableBlock;
    }

    @Override
    public boolean canConnectTo(BlockEntity connectTo) {
        return CapabilityUtils.energyStorageCapability(connectTo) != null
                || CapabilityUtils.blockEntityCapability(Capabilities.EnergyStorage.BLOCK, connectTo) != null;
    }
}
