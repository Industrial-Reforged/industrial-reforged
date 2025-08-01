package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyHandler;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.world.item.ItemStack;

public interface IEnergyItem {
    default IEnergyHandler getEnergyCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
    }

    // We have to pass the energy storage here, as it is not assigned to the capability yet
    default void initEnergyStorage(IEnergyHandler energyStorage, ItemStack itemStack) {
    }

    default void onEnergyChanged(ItemStack itemStack, int oldAmount) {
    }

    int getDefaultEnergyCapacity();

    EnergyTier getEnergyTier();
}
