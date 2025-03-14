package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import net.minecraft.world.item.ItemStack;

public interface IHeatItem {
    default IHeatStorage getHeatCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.HeatStorage.ITEM);
    }

    default void onHeatChanged(ItemStack itemStack, float oldAmount) {
    }

    float getDefaultHeatCapacity();
}
