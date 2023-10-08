package com.indref.industrial_reforged.api.energy.items;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Interface for implementing Items that store EU
 */
public interface IEnergyItem {
    default IEnergyStorage getEnergyStorage(ItemStack itemStack) {
        AtomicReference<IEnergyStorage> eStorage = new AtomicReference<>();
        itemStack.getCapability(IRCapabilities.ENERGY).ifPresent((eStorage::set));
        return eStorage.get();
    }
}
