package com.indref.industrial_reforged.api.blocks.generator;

import com.indref.industrial_reforged.api.blocks.machine.MachineBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNets;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.blocks.CableBlock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.indref.industrial_reforged.util.EnergyNetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public abstract class GeneratorBlockEntity extends MachineBlockEntity {
    public GeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos p_155229_, BlockState p_155230_) {
        super(blockEntityType, p_155229_, p_155230_);
        addEnergyStorage(getEnergyTier());
    }

    public abstract EnergyTier getEnergyTier();

    @Override
    public void serverTick() {
        EnergyNets energyNets = EnergyNetUtils.getEnergyNets((ServerLevel) level).getEnets();
        IEnergyStorage thisEnergyStorage = CapabilityUtils.energyStorageCapability(this);
        thisEnergyStorage.tryFillEnergy(getGenerationAmount());

        for (BlockPos offsetPos : BlockUtils.getBlocksAroundSelf(worldPosition)) {
            BlockEntity blockEntity1 = level.getBlockEntity(offsetPos);
            BlockState block = level.getBlockState(offsetPos);
            EnergyTier tier = getEnergyTier();
            if (block.getBlock() instanceof CableBlock) {
                Optional<EnergyNet> enet = energyNets.getNetwork(offsetPos);
                if (enet.isPresent() && enet.get().distributeEnergy(getGenerationAmount())) {
                    thisEnergyStorage.tryDrainEnergy(tier.getMaxOutput());
                }
            } else {
                if (blockEntity1 != null) {
                    IEnergyStorage energyStorage1 = CapabilityUtils.blockEntityCapability(IRCapabilities.EnergyStorage.BLOCK, blockEntity1);
                    if (energyStorage1 != null) {
                        thisEnergyStorage.tryDrainEnergy(tier.getMaxOutput());
                        energyStorage1.tryFillEnergy(tier.getMaxOutput());
                    }
                }
            }
        }
    }

    public abstract int getGenerationAmount();
}
