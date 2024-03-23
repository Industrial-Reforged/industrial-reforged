package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.data.heat.IHeatStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Interface for implementing BlockEntities that store EU
 * <p>
 * Note: if you want to check if a block is a heat block,
 * then use {@link com.indref.industrial_reforged.util.BlockUtils#isHeatBlock(BlockEntity)}
 * instead of an `instanceof` check, since all Blockentities that inherit {@link ContainerBlockEntity}
 * are IHeatBlocks
 */
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
