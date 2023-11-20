package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.registries.blockentities.CableBlockEntity;
import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.api.tiers.templates.EnergyTier;
import com.indref.industrial_reforged.capabilities.energy.network.IEnergyNets;
import com.indref.industrial_reforged.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CableBlock extends PipeBlock {
    private final EnergyTier energyTier;
    public CableBlock(Properties properties, EnergyTier energyTier) {
        super(properties);
        this.energyTier = energyTier;
    }

    @Override
    public void onPlace(BlockState p_60566_, Level level, BlockPos blockPos, BlockState p_60569_, boolean p_60570_) {
        IEnergyNets nets = Util.getEnergyNets(level);
        EnergyNet net = nets.getOrCreateNetAndPush(blockPos);
        for (BlockPos pos : BlockUtils.getBlocksAroundSelf(blockPos)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CableBlockEntity) {
                if (nets.getNetwork(pos) != net){
                    nets.mergeNets(net, nets.getNetwork(pos));
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState p_60518_, boolean p_60519_) {
        IEnergyNets nets = Util.getEnergyNets(level);
        EnergyNet net = nets.getNetwork(blockPos);
        // FIXME: throws null pointer exception
        assert net != null;
        if (net.get(EnergyNet.EnergyTypes.TRANSMITTERS).size() > 1) {
            net.remove(blockPos, EnergyNet.EnergyTypes.TRANSMITTERS);
        } else {
            nets.removeNetwork(blockPos);
        }
    }

    @Override
    public void neighborChanged(BlockState p_60509_, Level p_60510_, BlockPos p_60511_, Block p_60512_, BlockPos p_60513_, boolean p_60514_) {
        IndustrialReforged.LOGGER.info("Neighbor changed");
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        CableBlockEntity cable = (CableBlockEntity) level.getBlockEntity(blockPos);
        assert cable != null;
        player.sendSystemMessage(Component.literal("Energy Net: "+cable.getEnergyNet(level)));
        player.sendSystemMessage(Component.literal("Energy Nets: "+Util.getEnergyNets(level)));
        return InteractionResult.SUCCESS;
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
