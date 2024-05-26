package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
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
    default BlockEntity heatBlockEntity() {
        if (this instanceof BlockEntity blockEntity) {
            return blockEntity;
        }
        throw new Error(this.getClass()+ "is not an instanceof blockentity even though IEnergyBlock is implemented for the class");
    }

    default void setHeatStored(int value) {
        if (getHeatStored() == value) return;

        IHeatStorage heatStorage = heatBlockEntity().getLevel().getCapability(IRCapabilities.HeatStorage.BLOCK, heatBlockEntity().getBlockPos(), null);
        heatStorage.setHeatStored(value);
        onHeatChanged();
    }

    default int getHeatStored() {
        IHeatStorage heatStorage = heatBlockEntity().getLevel().getCapability(IRCapabilities.HeatStorage.BLOCK, heatBlockEntity().getBlockPos(), null);
        return heatStorage.getHeatStored();
    }

    default boolean canAcceptHeat(int amount) {
        return getHeatStored() + amount < getHeatCapacity();
    }

    int getHeatCapacity();

    default void onHeatChanged() {
    }

    default int tryDrainHeat(int value) {
        if (getHeatStored() - value >= 0) {
            setHeatStored(getHeatStored() - value);
            return 0;
        }
        int stored = getHeatStored();
        setHeatStored(0);
        return value - stored;
    }

    default int tryFillHeat(int value) {
        if (getHeatStored() + value <= getHeatCapacity()) {
            setHeatStored(getHeatStored() + value);
            return 0;
        }
        int stored = getHeatStored();
        setHeatStored(getHeatCapacity());
        return value - stored;
    }

    static boolean canAcceptHeatFromSide(BlockEntity blockEntity, Direction direction) {
        IndustrialReforged.LOGGER.debug("Cap: "+blockEntity.getLevel().getCapability(IRCapabilities.HeatStorage.BLOCK, blockEntity.getBlockPos(), direction));
        return false;
    }
}
