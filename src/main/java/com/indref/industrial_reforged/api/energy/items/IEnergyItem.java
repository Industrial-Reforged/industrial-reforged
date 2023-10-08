package com.indref.industrial_reforged.api.energy.items;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Interface for implementing Items that store EU
 */
public interface IEnergyItem {
    default IEnergyStorage getEnergyStorage() {
        if (this instanceof Item item) {
            LazyOptional<IEnergyStorage> cap = item.getDefaultInstance().getCapability(IRCapabilities.ENERGY);
            if (cap.isPresent())
                return cap.orElseThrow(NullPointerException::new);
        }
        return null;
    }

    default void setStoredEnergy(ItemStack itemStack, int value) {
        if (this instanceof Item) {
            IndustrialReforged.LOGGER.info("I am an item!!!"+this);
        }
        getEnergyStorage().setEnergyStored(value);
    }

    /**
     * This needs to be overridden to give it functionality
     * Might want to change this, so it's not as confusing
     */
    default void setStoredEnergy(int value) {

    }

    default void tryDrainEnergy(int value) {

    }

    default void tryFillEnergy(int value) {

    }
}
