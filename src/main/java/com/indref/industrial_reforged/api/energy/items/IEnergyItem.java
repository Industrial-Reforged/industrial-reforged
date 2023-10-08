package com.indref.industrial_reforged.api.energy.items;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Interface for implementing Items that store EU
 */
public interface IEnergyItem {
    default IEnergyStorage getEnergyStorage(ItemStack itemStack) {
        LazyOptional<IEnergyStorage> cap = itemStack.getCapability(IRCapabilities.ENERGY);
        if(cap.isPresent())
            return cap.orElseThrow( NullPointerException::new);
        return null;
    }
}
