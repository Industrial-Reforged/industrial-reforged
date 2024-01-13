package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.data.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.world.item.ItemStack;

public interface IEnergyItem {
    private static IEnergyStorage getCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
    }

    default int getEnergyStored(ItemStack itemStack) {
        return getCap(itemStack).getEnergyStored();
    }

    default void setEnergyStored(ItemStack itemStack, int value) {
        getCap(itemStack).setEnergyStored(value);
    }

    default int getEnergyCapacity() {
       return getEnergyTier().getDefaultCapacity();
    }

    EnergyTier getEnergyTier();
}
