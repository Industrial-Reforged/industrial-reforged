package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.IScannable;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.capabilities.energy.IEnergyStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Interface for implementing BlockEntities that store EU
 */
public interface IEnergyBlock extends IScannable {

    default void setEnergyStored(BlockEntity blockEntity, int value) {
        int prev = getEnergyStored(blockEntity);
        if (prev == value) return;

        IEnergyStorage energyStorage = blockEntity.getLevel().getCapability(IRCapabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(), null);
        energyStorage.setEnergyStored(value);
        onEnergyChanged();
    }

    default int getEnergyStored(BlockEntity blockEntity) {
        IEnergyStorage energyStorage = blockEntity.getLevel().getCapability(IRCapabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(), null);
        return energyStorage.getEnergyStored();
    }

    default boolean canAcceptEnergy(BlockEntity blockEntity, int amount) {
        return getEnergyStored(blockEntity) + amount < getEnergyCapacity();
    }

    default int getEnergyCapacity() {
        return getEnergyTier().getDefaultCapacity();
    }

    default void onEnergyChanged() {
    }

    default boolean tryDrainEnergy(BlockEntity blockEntity, int value) {
        if (getEnergyStored(blockEntity) - value >= 0) {
            setEnergyStored(blockEntity, getEnergyStored(blockEntity) - value);
            return true;
        }
        return false;
    }

    default boolean tryFillEnergy(BlockEntity blockEntity, int value) {
        if (getEnergyStored(blockEntity) + value <= getEnergyCapacity()) {
            setEnergyStored(blockEntity, getEnergyStored(blockEntity) + value);
            return true;
        } else {
            setEnergyStored(blockEntity, getEnergyCapacity());
        }
        return false;
    }

    EnergyTier getEnergyTier();

    static boolean canAcceptEnergyFromSide(BlockEntity blockEntity, Direction direction) {
        IndustrialReforged.LOGGER.debug("Cap: "+blockEntity.getLevel().getCapability(IRCapabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(), direction));
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
                Component.translatable("scanner_info.energy_block.energy_ratio")
                        .withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(String.format("%d/%d", energyBlock.getEnergyStored(blockEntity), energyBlock.getEnergyCapacity())))
                        .withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(","))
                        .withStyle(ChatFormatting.WHITE)
        );
    }
}