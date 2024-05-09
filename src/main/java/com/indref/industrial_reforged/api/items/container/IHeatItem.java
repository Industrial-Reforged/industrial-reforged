package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.heat.storage.IHeatStorage;
import net.minecraft.world.item.ItemStack;

public interface IHeatItem {
    private static IHeatStorage getCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.HeatStorage.ITEM);
    }

    default int getHeatStored(ItemStack itemStack) {
        return getCap(itemStack).getHeatStored();
    }

    default void setHeatStored(ItemStack itemStack, int value) {
        getCap(itemStack).setHeatStored(value);
    }

    int getHeatCapacity(ItemStack itemStack);
}
