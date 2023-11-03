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

// TODO: 10/26/2023 make drain methods static
public interface IEnergyItem extends IContainerItem {
    default IEnergyStorage getEnergyStorage(ItemStack itemStack) {
        LazyOptional<IEnergyStorage> cap = itemStack.getCapability(IRCapabilities.ENERGY);
        if (cap.isPresent())
            return cap.orElseThrow(NullPointerException::new);

        return null;
    }

    @Override
    default void setStored(ItemStack itemStack, int value) {
        getEnergyStorage(itemStack).setEnergyStored(value);
    }

    @Override
    default int getStored(ItemStack itemStack) {
        return getEnergyStorage(itemStack).getEnergyStored();
    }

    /**
     * Try draining energy from an item
     * @param itemStack the itemstack you want to drain the energy from
     * @param value the amount of energy you want drain
     * @return whether the draining was successful (true) or not (false)
     */
    @Override
    default boolean tryDrain(ItemStack itemStack, int value) {
        if (getStored(itemStack)+value > 0) {
            setStored(itemStack, getStored(itemStack)-value);
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
    @Override
    default boolean tryFill(ItemStack itemStack, int value) {
        if (getStored(itemStack)+value < getCapacity()) {
            setStored(itemStack, getStored(itemStack)+value);
            return true;
        }
        return false;
    }
}
