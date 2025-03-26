package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

public interface IEnergyItem {
    default IEnergyStorage getEnergyCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
    }

    // We have to pass the energy storage here, as it is not fully initialized yet
    default void initEnergyStorage(IEnergyStorage energyStorage, ItemStack itemStack) {
    }

    default void onEnergyChanged(ItemStack itemStack, int oldAmount) {
    }

    default int getDefaultEnergyCapacity() {
       return getEnergyTier().value().defaultCapacity();
    }

    Holder<EnergyTier> getEnergyTier();
}
