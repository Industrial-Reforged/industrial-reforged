package com.indref.industrial_reforged.api.blocks;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.networking.IRPackets;
import com.indref.industrial_reforged.networking.packets.S2CEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Interface for implementing Blocks that store EU
 */
public interface IEnergyBlock extends IScannable {
    EnergyStorageProvider getEnergyStorage();

    default void onEnergyChanged() {
    }

    default void setEnergyStored(BlockEntity blockEntity, int value) {
        IEnergyStorage energyStorage = blockEntity.getCapability(IRCapabilities.ENERGY).orElseThrow(NullPointerException::new);
        energyStorage.setEnergyStored(value);
        onEnergyChanged();
    }

    default int getEnergyStored(BlockEntity blockEntity) {
        IEnergyStorage energyStorage = blockEntity.getCapability(IRCapabilities.ENERGY).orElseThrow(NullPointerException::new);
        return energyStorage.getEnergyStored();
    }

    default int getEnergyCapacity(BlockEntity blockEntity) {
        IEnergyStorage energyStorage = blockEntity.getCapability(IRCapabilities.ENERGY).orElseThrow(NullPointerException::new);
        return energyStorage.getEnergyCapacity();
    }

    @Override
    default List<Component> displayText(BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        IEnergyBlock energyBlock = null;
        BlockEntity blockEntity = level.getBlockEntity(scannedBlockPos);
        if (blockEntity instanceof IEnergyBlock energyBlock1)
            energyBlock = energyBlock1;

        return List.of(
                scannedBlock.getBlock().getName(),
                MutableComponent.create(ComponentContents.EMPTY)
                        .append(Component.translatable("scanner_info.energy_block.energy_ratio"))
                        .append(Component.literal(String.format("%d/%d", energyBlock.getEnergyStored(blockEntity), energyBlock.getEnergyCapacity(blockEntity))))
                        .append(Component.literal(",")),
                Component.literal(String.valueOf(energyBlock.getEnergyStored(blockEntity)))
        );
    }
}
