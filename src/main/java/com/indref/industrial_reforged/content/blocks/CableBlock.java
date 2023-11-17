package com.indref.industrial_reforged.content.blocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.IWrenchable;
import com.indref.industrial_reforged.api.blocks.container.IContainerBlock;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.content.blockentities.CableBlockEntity;
import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.api.tiers.templates.EnergyTier;
import com.indref.industrial_reforged.capabilities.energy.network.IEnergyNets;
import com.indref.industrial_reforged.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CableBlock extends PipeBlock {
    private final EnergyTier energyTier;
    public CableBlock(Properties properties, EnergyTier energyTier) {
        super(properties);
        this.energyTier = energyTier;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean p_60570_) {
        // perform this check to ensure that block is actually placed and not just block states updating
        if (oldState.is(Blocks.AIR)) {
            IEnergyNets nets = Util.getEnergyNets(level);
            EnergyNet net = nets.getOrCreateNetAndPush(blockPos);
            for (BlockPos pos : BlockUtils.getBlocksAroundSelf(blockPos)) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof CableBlockEntity) {
                    if (nets.getNetwork(pos) != net) {
                        nets.mergeNets(net, nets.getNetwork(pos));
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        // perform this check to ensure that block is actually removed and not just block states updating
        if (newState.is(Blocks.AIR)) {
            IEnergyNets nets = Util.getEnergyNets(level);
            EnergyNet net = nets.getNetwork(blockPos);

            if (net != null) {
                if (net.get(EnergyNet.EnergyTypes.TRANSMITTERS).size() > 1) {
                    net.remove(blockPos, EnergyNet.EnergyTypes.TRANSMITTERS);
                } else {
                    nets.removeNetwork(blockPos);
                }
            } else {
                IndustrialReforged.LOGGER.error("net at: {} is null", blockPos);
            }
        }
    }

    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CableBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean canConnectToPipe(Block connectTo) {
        return connectTo instanceof CableBlock;
    }

    @Override
    public boolean canConnectTo(BlockEntity connectTo) {
        return connectTo instanceof IEnergyBlock && !(connectTo instanceof CableBlockEntity);
    }
}
