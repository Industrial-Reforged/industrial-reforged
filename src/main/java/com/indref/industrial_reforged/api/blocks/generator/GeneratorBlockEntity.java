package com.indref.industrial_reforged.api.blocks.generator;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNets;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.networking.data.EnergySyncData;
import com.indref.industrial_reforged.registries.blocks.CableBlock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class GeneratorBlockEntity extends BlockEntity implements IEnergyBlock {
    public GeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos p_155229_, BlockState p_155230_) {
        super(blockEntityType, p_155229_, p_155230_);
    }

    @Override
    public void onEnergyChanged() {
        if (!level.isClientSide()) {
            PacketDistributor.ALL.noArg().send(new EnergySyncData(getEnergyStored(this), worldPosition));
        }
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        EnergyNets energyNets = Utils.getEnergyNets((ServerLevel) level).getEnets();
        tryFillEnergy(this, getGenerationAmount());

        for (BlockPos offsetPos : BlockUtils.getBlocksAroundSelf(blockPos)) {
            BlockEntity blockEntity1 = level.getBlockEntity(offsetPos);
            BlockState block = level.getBlockState(offsetPos);
            EnergyTier energyTier = getEnergyTier();
            if (energyTier != null) {
                if (block.getBlock() instanceof CableBlock) {
                    EnergyNet enet = energyNets.getNetwork(offsetPos);
                    if (enet != null && enet.distributeEnergy(getGenerationAmount()))
                        tryDrainEnergy(this, energyTier.getMaxOutput());
                } else if (blockEntity1 instanceof IEnergyBlock energyBlock1) {
                    tryDrainEnergy(this, energyTier.getMaxOutput());
                    energyBlock1.tryFillEnergy(blockEntity1, energyTier.getMaxOutput());
                }
            } else {
                IndustrialReforged.LOGGER.error("{} at {} does not have a correct heat tier. Unable to produce heat", blockState.getBlock().getName().getString(), blockPos);
            }
        }
    }

    public abstract int getGenerationAmount();
}
