package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.Scannable;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.data.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface for implementing BlockEntities that store EU
 */
public interface IEnergyBlock {
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
        EnergyTier energyTier = getEnergyTier();
        if (energyTier != null)
            return energyTier.getDefaultCapacity();
        IndustrialReforged.LOGGER.error("{} does not provide a correct heat type (heat type is null) unable to get Capacity", this.getClass().getName());
        return -1;
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

    @Nullable
    EnergyTier getEnergyTier();

    static boolean canAcceptEnergyFromSide(BlockEntity blockEntity, Direction direction) {
        IndustrialReforged.LOGGER.debug("Cap: " + blockEntity.getLevel().getCapability(IRCapabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(), direction));
        return false;
    }
}