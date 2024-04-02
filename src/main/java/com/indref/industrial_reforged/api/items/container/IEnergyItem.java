package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.data.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IEnergyItem {
    private static IEnergyStorage getCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
    }

    default void onEnergyChanged(ItemStack itemStack) {
    }

    default int getEnergyStored(ItemStack itemStack) {
        return getCap(itemStack).getEnergyStored();
    }

    default void setEnergyStored(ItemStack itemStack, int value) {
        getCap(itemStack).setEnergyStored(value);
        onEnergyChanged(itemStack);
    }

    default int getEnergyCapacity() {
       return getEnergyTier().getDefaultCapacity();
    }

    EnergyTier getEnergyTier();

    default boolean tryDrainEnergy(ItemStack itemStack, int value) {
        if (getEnergyStored(itemStack) - value >= 0) {
            setEnergyStored(itemStack, getEnergyStored(itemStack) - value);
            return true;
        }
        return false;
    }

    default boolean tryFillEnergy(ItemStack itemStack, int value) {
        if (getEnergyStored(itemStack) + value <= getEnergyCapacity()) {
            setEnergyStored(itemStack, getEnergyStored(itemStack) + value);
            return true;
        } else {
            setEnergyStored(itemStack, getEnergyCapacity());
        }
        return false;
    }
}
