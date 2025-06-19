package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface IEnergyItem {
    default IEnergyStorage getEnergyCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
    }

    // We have to pass the energy storage here, as it is not fully initialized yet
    default void initEnergyStorage(IEnergyStorage energyStorage, ItemStack itemStack) {
    }

    default void onEnergyChanged(ItemStack itemStack, int oldAmount) {
    }

    int getDefaultEnergyCapacity();

    EnergyTier getEnergyTier();
}
