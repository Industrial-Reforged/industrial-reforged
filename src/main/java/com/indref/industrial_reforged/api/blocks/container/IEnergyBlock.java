package com.indref.industrial_reforged.api.blocks.container;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Interface for implementing BlockEntities that store EU
 * <p>
 * Note: if you want to check if a block is an energy block,
 * then use {@link com.indref.industrial_reforged.util.BlockUtils#isEnergyBlock(BlockEntity)}
 * instead of an `instanceof` check, since all Blockentities that inherit {@link ContainerBlockEntity}
 * are IEnergyBlocks
 */
public interface IEnergyBlock {
    default BlockEntity energyBlockEntity() {
        if (this instanceof BlockEntity blockEntity) {
            return blockEntity;
        }
        throw new Error(this.getClass()+ "is not an instanceof blockentity even though IEnergyBlock is implemented for the class");
    }

    default void setEnergyStored(int value) {
        int prev = getEnergyStored();
        if (prev == value) return;

        IEnergyStorage energyStorage = energyBlockEntity().getLevel().getCapability(IRCapabilities.EnergyStorage.BLOCK, energyBlockEntity().getBlockPos(), null);
        energyStorage.setEnergyStored(value);
        onEnergyChanged();
    }

    default int getEnergyStored() {
        IEnergyStorage energyStorage = energyBlockEntity().getLevel().getCapability(IRCapabilities.EnergyStorage.BLOCK, energyBlockEntity().getBlockPos(), null);
        return energyStorage.getEnergyStored();
    }

    default boolean canAcceptEnergy(int amount) {
        return getEnergyStored() + amount < getEnergyCapacity();
    }

    default int getEnergyCapacity() {
        return getEnergyTier().getDefaultCapacity();
    }

    default void onEnergyChanged() {
    }

    default boolean tryDrainEnergy(int value) {
        if (getEnergyStored() - value >= 0) {
            setEnergyStored(getEnergyStored() - value);
            return true;
        }
        return false;
    }

    default boolean tryFillEnergy(int value) {
        if (getEnergyStored() + value <= getEnergyCapacity()) {
            setEnergyStored(getEnergyStored() + value);
            return true;
        } else {
            setEnergyStored(getEnergyCapacity());
        }
        return false;
    }

    @NotNull
    EnergyTier getEnergyTier();

    static boolean canAcceptEnergyFromSide(BlockEntity blockEntity, Direction direction) {
        IndustrialReforged.LOGGER.debug("Cap: " + blockEntity.getLevel().getCapability(IRCapabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(), direction));
        return false;
    }
}