package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.capabilites.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;
import net.minecraft.world.item.ItemStack;

public interface IEnergyItem {
    default EnergyHandler getEnergyCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.ENERGY_ITEM);
    }

    // We have to pass the energy storage here, as it is not assigned to the capability yet
    default void initEnergyStorage(EnergyHandler energyStorage, ItemStack itemStack) {
    }

    default void onEnergyChanged(ItemStack itemStack, int oldAmount) {
    }

    default int getDefaultCapacity() {
        return this.getEnergyTier().defaultCapacity();
    }

    EnergyTierImpl getEnergyTier();
}
