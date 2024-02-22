package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnetsSavedData;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Utils;
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

import java.util.ArrayList;
import java.util.List;

public class CableBlock extends PipeBlock {
    private final EnergyTier energyTier;
    private List<BlockPos> checkedBlocks = new ArrayList<>();

    public CableBlock(Properties properties, int width, EnergyTier energyTier) {
        super(properties, width);
        this.energyTier = energyTier;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean p_60570_) {
        // perform this check to ensure that block is actually placed and not just block states updating
        if (oldState.is(Blocks.AIR) && level instanceof ServerLevel serverLevel) {
            EnetsSavedData nets = Utils.getEnergyNets(serverLevel);
            // Adds the net
            EnergyNet net = nets.getEnets().getOrCreateNetAndPush(blockPos);
            nets.setDirty();
            for (BlockPos pos : BlockUtils.getBlocksAroundSelf(blockPos)) {
                Block block = level.getBlockState(pos).getBlock();
                if (block instanceof CableBlock) {
                    if (nets.getEnets().getNetwork(pos) != net) {
                        nets.getEnets().mergeNets(net, nets.getEnets().getNetwork(pos));
                        nets.setDirty();
                    }
                } else if (level.getBlockEntity(pos) instanceof IEnergyBlock) {
                    nets.getEnets().getNetwork(blockPos).add(pos, EnergyNet.EnergyTypes.INTERACTORS);
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        // perform this check to ensure that block is actually removed and not just block states updating
        if (newState.is(Blocks.AIR) && level instanceof ServerLevel serverLevel) {
            super.onRemove(blockState, level, blockPos, newState, p_60519_);
            EnetsSavedData nets = Utils.getEnergyNets(serverLevel);
            nets.getEnets().splitNets(blockPos);
            nets.getEnets().removeNetwork(blockPos);
            // Tell the level to save it
            nets.setDirty();
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction facingDirection, BlockState facingBlockState, LevelAccessor level, BlockPos blockPos, BlockPos facingBlockPos) {
        if (level.getBlockEntity(facingBlockPos) instanceof IEnergyBlock && level instanceof ServerLevel serverLevel) {
            Utils.getEnergyNets(serverLevel).getEnets().getNetwork(blockPos).add(facingBlockPos, EnergyNet.EnergyTypes.INTERACTORS);
        }
        return super.updateShape(blockState, facingDirection, facingBlockState, level, blockPos, facingBlockPos);
    }

    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    @Override
    public boolean canConnectToPipe(Block connectTo) {
        return connectTo instanceof CableBlock;
    }

    @Override
    public boolean canConnectTo(BlockEntity connectTo) {
        return (connectTo instanceof IEnergyBlock) || (connectTo.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, connectTo.getBlockPos(), null) != null);
    }
}
