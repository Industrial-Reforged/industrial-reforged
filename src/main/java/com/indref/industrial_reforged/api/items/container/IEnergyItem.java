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

    default void onEnergyChanged(ItemStack itemStack, int oldAmount) {
    }

    default int getDefaultEnergyCapacity() {
       return getEnergyTier().value().getDefaultCapacity();
    }

    Holder<EnergyTier> getEnergyTier();
}
