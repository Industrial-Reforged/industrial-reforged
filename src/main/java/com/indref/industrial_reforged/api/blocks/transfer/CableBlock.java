package com.indref.industrial_reforged.api.blocks.transfer;

import com.indref.industrial_reforged.api.tiers.templates.EnergyTier;
import com.indref.industrial_reforged.capabilities.energy.network.IEnergyNets;
import com.indref.industrial_reforged.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
        nets.getOrCreateNetwork(blockPos);
    }

    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CableBlockEntity(blockPos, blockState);
    }
}
