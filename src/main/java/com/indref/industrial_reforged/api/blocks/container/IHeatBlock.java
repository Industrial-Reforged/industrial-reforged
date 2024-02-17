package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.Scannable;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.data.heat.IHeatStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IHeatBlock {
    default void setHeatStored(BlockEntity blockEntity, int value) {
        int prev = getHeatStored(blockEntity);
        if (prev == value) return;

        IHeatStorage heatStorage = blockEntity.getLevel().getCapability(IRCapabilities.HeatStorage.BLOCK, blockEntity.getBlockPos(), null);
        heatStorage.setHeatStored(value);
        onHeatChanged();
    }

    default int getHeatStored(BlockEntity blockEntity) {
        IHeatStorage heatStorage = blockEntity.getLevel().getCapability(IRCapabilities.HeatStorage.BLOCK, blockEntity.getBlockPos(), null);
        return heatStorage.getHeatStored();
    }

    default boolean canAcceptHeat(BlockEntity blockEntity, int amount) {
        return getHeatStored(blockEntity) + amount < getHeatCapacity();
    }

    int getHeatCapacity();

    default void onHeatChanged() {
    }

    default boolean tryDrainHeat(BlockEntity blockEntity, int value) {
        if (getHeatStored(blockEntity) - value >= 0) {
            setHeatStored(blockEntity, getHeatStored(blockEntity) - value);
            return true;
        }
        return false;
    }

    default boolean tryFillHeat(BlockEntity blockEntity, int value) {
        if (getHeatStored(blockEntity) + value <= getHeatCapacity()) {
            setHeatStored(blockEntity, getHeatStored(blockEntity) + value);
            return true;
        } else {
            setHeatStored(blockEntity, getHeatCapacity());
        }
        return false;
    }

    static boolean canAcceptHeatFromSide(BlockEntity blockEntity, Direction direction) {
        IndustrialReforged.LOGGER.debug("Cap: "+blockEntity.getLevel().getCapability(IRCapabilities.HeatStorage.BLOCK, blockEntity.getBlockPos(), direction));
        return false;
    }
}
