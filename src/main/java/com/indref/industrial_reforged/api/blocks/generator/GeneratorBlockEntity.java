package com.indref.industrial_reforged.api.blocks.generator;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNets;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.blocks.CableBlock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.EnergyNetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public abstract class GeneratorBlockEntity extends ContainerBlockEntity {
    public GeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos p_155229_, BlockState p_155230_) {
        super(blockEntityType, p_155229_, p_155230_);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        EnergyNets energyNets = EnergyNetUtils.getEnergyNets((ServerLevel) level).getEnets();
        tryFillEnergy(this, getGenerationAmount());

        for (BlockPos offsetPos : BlockUtils.getBlocksAroundSelf(blockPos)) {
            BlockEntity blockEntity1 = level.getBlockEntity(offsetPos);
            BlockState block = level.getBlockState(offsetPos);
            Optional<EnergyTier> energyTier = getEnergyTier();
            if (energyTier.isPresent()) {
                EnergyTier tier = energyTier.get();
                if (block.getBlock() instanceof CableBlock) {
                    Optional<EnergyNet> enet = energyNets.getNetwork(offsetPos);
                    if (enet.isPresent() && enet.get().distributeEnergy(getGenerationAmount()))
                        tryDrainEnergy(this, tier.getMaxOutput());
                } else if (blockEntity1 instanceof IEnergyBlock energyBlock1) {
                    tryDrainEnergy(this, tier.getMaxOutput());
                    energyBlock1.tryFillEnergy(blockEntity1, tier.getMaxOutput());
                }
            } else {
                IndustrialReforged.LOGGER.error("{} at {} does not have a correct heat tier. Unable to produce heat", blockState.getBlock().getName().getString(), blockPos);
            }
        }
    }

    public abstract int getGenerationAmount();
}
