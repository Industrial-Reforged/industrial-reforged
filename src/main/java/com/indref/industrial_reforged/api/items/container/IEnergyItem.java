package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

public interface IEnergyItem {
    static IEnergyStorage getCap(ItemStack itemStack) {
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

    default int getEnergyCapacity(ItemStack itemStack) {
       return getEnergyTier().value().getDefaultCapacity();
    }

    default void setEnergyCapacity(ItemStack itemStack, int value) {
        getCap(itemStack).setEnergyCapacity(value);
    }

    Holder<EnergyTier> getEnergyTier();

    default int tryDrainEnergy(ItemStack itemStack, int value) {
        return getCap(itemStack).tryDrainEnergy(value, false);
    }

    default int tryFillEnergy(ItemStack itemStack, int value) {
        return getCap(itemStack).tryFillEnergy(value, false);
    }
}
