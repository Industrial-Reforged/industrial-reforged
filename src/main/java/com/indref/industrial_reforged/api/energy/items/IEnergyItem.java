package com.indref.industrial_reforged.api.energy.items;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Interface for implementing Items that store EU.
 * IMPORTANT! When implementing this manually for an item,
 * you need to override the getUseDuration() function and make it return 1
 */
public interface IEnergyItem {
    default IEnergyStorage getEnergyStorage(ItemStack itemStack) {
        LazyOptional<IEnergyStorage> cap = itemStack.getCapability(IRCapabilities.ENERGY);
        if (cap.isPresent())
            return cap.orElseThrow(NullPointerException::new);

        return null;
    }

    default void setStoredEnergy(ItemStack itemStack, int value) {
        getEnergyStorage(itemStack).setEnergyStored(value);
    }

    default void setMaxEnergy(ItemStack itemStack) {
        getEnergyStorage(itemStack).setMaxEnergy(getMaxEnergy());
    }

    int getMaxEnergy();

    default int getStoredEnergy(ItemStack itemStack) {
        return getEnergyStorage(itemStack).getEnergyStored();
    }

    /**
     * Try draining energy from an item
     * @param itemStack the itemstack you want to drain the energy from
     * @param value the amount of energy you want drain
     * @return whether the draining was successful (true) or not (false)
     */
    default boolean tryDrainEnergy(ItemStack itemStack, int value) {
        if (getStoredEnergy(itemStack)+value > 0) {
            setStoredEnergy(itemStack, getStoredEnergy(itemStack)-value);
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
        if (getStoredEnergy(itemStack)+value < getMaxEnergy()) {
            setStoredEnergy(itemStack, getStoredEnergy(itemStack)+value);
            return true;
        }
        return false;
    }
}
