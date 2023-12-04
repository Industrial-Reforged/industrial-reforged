package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.capabilities.energy.storage.IEnergyStorage;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.LazyOptional;

/**
 * Interface for implementing Items that store EU.
 * IMPORTANT! When implementing this manually for an item,
 * you need to override the getUseDuration() function and make it return 1
 */

public interface IEnergyItem {
    static IEnergyStorage getEnergyStorage(ItemStack itemStack) {
        LazyOptional<IEnergyStorage> cap = itemStack.getCapability(IRCapabilities.ENERGY);
        if (cap.isPresent())
            return cap.orElseThrow(NullPointerException::new);

        return null;
    }

    default void setEnergyStored(ItemStack itemStack, int value) {
        getEnergyStorage(itemStack).setEnergyStored(value);
    }

    default int getEnergyStored(ItemStack itemStack) {
        return getEnergyStorage(itemStack).getEnergyStored();
    }

    int getEnergyCapacity();

    /**
     * Try draining energy from an item
     * @param itemStack the itemstack you want to drain the energy from
     * @param value the amount of energy you want drain
     * @return whether the draining was successful (true) or not (false)
     */
    default boolean tryDrainEnergy(ItemStack itemStack, int value) {
        if (getEnergyStored(itemStack)-value >= 0) {
            setEnergyStored(itemStack, getEnergyStored(itemStack)-value);
            return true;
        }
        return false;
    }

    /**
     * Try filling energy of an item
     * @param itemStack the itemstack you want to fill the energy to
     * @param value the amount of energy you want fill
     * @return whether the filling was successful (true) or not (false)
     */
    default boolean tryFillEnergy(ItemStack itemStack, int value) {
        if (getEnergyStored(itemStack)+value <= getEnergyCapacity()) {
            setEnergyStored(itemStack, getEnergyStored(itemStack)+value);
            return true;
        }
        return false;
    }
}
