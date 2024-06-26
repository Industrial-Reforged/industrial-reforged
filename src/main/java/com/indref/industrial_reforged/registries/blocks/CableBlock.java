package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnetsSavedData;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.BlockUtils;
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
            EnetsSavedData nets = EnergyNetUtils.getEnergyNets(serverLevel);
            // Adds the net
            EnergyNet net = nets.getEnets().getOrCreateNetAndPush(blockPos);
            nets.setDirty();
            for (BlockPos pos : BlockUtils.getBlocksAroundSelf(blockPos)) {
                Block block = level.getBlockState(pos).getBlock();
                if (block instanceof CableBlock) {
                    Optional<EnergyNet> network = nets.getEnets().getNetwork(pos);
                    if (network.isPresent() && network.get() != net) {
                        nets.getEnets().mergeNets(net, network.get());
                        nets.setDirty();
                    }
                } else if (BlockUtils.isEnergyBlock(level.getBlockEntity(pos))) {
                    Optional<EnergyNet> network = nets.getEnets().getNetwork(blockPos);
                    network.ifPresent(energyNet -> energyNet.add(pos, EnergyNet.EnergyTypes.INTERACTORS));
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        // perform this check to ensure that block is actually removed and not just block states updating
        if (newState.is(Blocks.AIR) && level instanceof ServerLevel serverLevel) {
            super.onRemove(blockState, level, blockPos, newState, p_60519_);
            EnetsSavedData nets = EnergyNetUtils.getEnergyNets(serverLevel);
            nets.getEnets().splitNets(blockPos);
            nets.getEnets().removeNetwork(blockPos);
            // Tell the level to save it
            nets.setDirty();
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction facingDirection, BlockState facingBlockState, LevelAccessor level, BlockPos blockPos, BlockPos facingBlockPos) {
        if (level instanceof ServerLevel serverLevel) {
            Optional<EnergyNet> network = EnergyNetUtils.getEnergyNets(serverLevel).getEnets().getNetwork(blockPos);
            if (BlockUtils.isEnergyBlock(level.getBlockEntity(facingBlockPos))) {
                network.ifPresent(net -> net.add(facingBlockPos, EnergyNet.EnergyTypes.INTERACTORS));
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
        return BlockUtils.isEnergyBlock(connectTo) || BlockUtils.getBlockEntityCapability(Capabilities.EnergyStorage.BLOCK, connectTo).isPresent();
    }
}
