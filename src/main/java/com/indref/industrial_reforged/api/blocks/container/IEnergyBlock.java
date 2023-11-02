package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.api.blocks.IScannable;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Interface for implementing Blocks that store EU
 */
// TODO: 11/2/23 make methods static 
public interface IEnergyBlock extends IContainerBlock, IScannable {
    EnergyStorageProvider getEnergyStorage();

    default void onEnergyChanged() {
    }

    @Override
    default void setStored(BlockEntity blockEntity, int value) {
        IEnergyStorage energyStorage = blockEntity.getCapability(IRCapabilities.ENERGY).orElseThrow(NullPointerException::new);
        energyStorage.setEnergyStored(value);
        onEnergyChanged();
    }

    @Override
    default void setCapacity(BlockEntity blockEntity, int value) {
        IEnergyStorage energyStorage = blockEntity.getCapability(IRCapabilities.ENERGY).orElseThrow(NullPointerException::new);
        energyStorage.setEnergyCapacity(value);
        onEnergyChanged();
    }

    @Override
    default int getStored(BlockEntity blockEntity) {
        IEnergyStorage energyStorage = blockEntity.getCapability(IRCapabilities.ENERGY).orElseThrow(NullPointerException::new);
        return energyStorage.getEnergyStored();
    }

    @Override
    default boolean tryDrain(BlockEntity blockEntity, int value) {
        if (getStored(blockEntity)-value >= 0) {
            setStored(blockEntity, getStored(blockEntity)-value);
            return true;
        }
        return false;
    }

    @Override
    default boolean tryFill(BlockEntity blockEntity, int value) {
        if (getStored(blockEntity)+value <= getCapacity(blockEntity)) {
            setStored(blockEntity, getStored(blockEntity)+value);
            return true;
        }
        return false;
    }

    @Override
    default List<Component> displayText(BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        IEnergyBlock energyBlock = null;
        BlockEntity blockEntity = level.getBlockEntity(scannedBlockPos);
        if (blockEntity instanceof IEnergyBlock energyBlock1)
            energyBlock = energyBlock1;

        return List.of(
                scannedBlock.getBlock().getName().withStyle(ChatFormatting.WHITE),
                MutableComponent.create(ComponentContents.EMPTY).withStyle(ChatFormatting.WHITE)
                        .append(Component.translatable("scanner_info.energy_block.energy_ratio"))
                        .append(Component.literal(String.format("%d/%d", energyBlock.getStored(blockEntity), energyBlock.getCapacity(blockEntity))))
                        .append(Component.literal(","))
        );
    }
}
