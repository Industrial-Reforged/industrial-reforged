package com.indref.industrial_reforged.api.blocks.generator;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNets;
import com.indref.industrial_reforged.networking.IRPackets;
import com.indref.industrial_reforged.networking.packets.S2CEnergySync;
import com.indref.industrial_reforged.registries.blocks.CableBlock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GeneratorBlockEntity extends BlockEntity implements IEnergyBlock {
    public GeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos p_155229_, BlockState p_155230_) {
        super(blockEntityType, p_155229_, p_155230_);
    }

    @Override
    public void onEnergyChanged() {
        if (!level.isClientSide()) {
            IRPackets.sendToClients(new S2CEnergySync(getEnergyStored(this), worldPosition));
        }
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        EnergyNets energyNets = Util.getEnergyNets((ServerLevel) level).getEnets();
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IEnergyBlock energyBlock = (IEnergyBlock) blockEntity;
        energyBlock.tryFillEnergy(blockEntity, getGenerationAmount());

        for (BlockPos pos : BlockUtils.getBlocksAroundSelf(blockPos)) {
            BlockEntity blockEntity1 = level.getBlockEntity(pos);
            BlockState block = level.getBlockState(pos);
            if (block.getBlock() instanceof CableBlock cableBlock) {
                IndustrialReforged.LOGGER.debug("Found cable");
                EnergyNet enet = energyNets.getNetwork(pos);
                try {
                    if (enet.distributeEnergy(energyBlock.getEnergyTier().getMaxOutput())) {
                        energyBlock.tryDrainEnergy(blockEntity, energyBlock.getEnergyTier().getMaxOutput());
                    }
                } catch (Exception ignored) {
                }
            } else if (blockEntity1 instanceof IEnergyBlock energyBlock1) {
                energyBlock.tryDrainEnergy(blockEntity, energyBlock.getEnergyTier().getMaxOutput());
            }
        }
    }

    public abstract int getGenerationAmount();
}
