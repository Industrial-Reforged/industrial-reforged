package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.capabilites.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import net.minecraft.world.item.ItemStack;

public interface IHeatItem {
    default HeatStorage getHeatCap(ItemStack itemStack) {
        return itemStack.getCapability(IRCapabilities.HEAT_ITEM);
    }

    default void onHeatChanged(ItemStack itemStack, float oldAmount) {
    }

    float getDefaultCapacity();
}
