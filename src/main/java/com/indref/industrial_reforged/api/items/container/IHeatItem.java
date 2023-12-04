package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.capabilities.heat.IHeatStorage;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.LazyOptional;

public interface IHeatItem {
    static IHeatStorage getHeatStorage(ItemStack itemStack) {
        LazyOptional<IHeatStorage> cap = itemStack.getCapability(IRCapabilities.HEAT);
        if (cap.isPresent())
            return cap.orElseThrow(NullPointerException::new);

        return null;
    }

    default void setHeatStored(ItemStack itemStack, int value) {
        IHeatStorage heatStorage = getHeatStorage(itemStack);
        if (heatStorage != null) {
            heatStorage.setHeatStored(value);
        }
    }

    default int getHeatStored(ItemStack itemStack) {
        IHeatStorage heatStorage = getHeatStorage(itemStack);
        if (heatStorage != null) {
            return heatStorage.getHeatStored();
        }
        return -1;
    }

    int getHeatCapacity();

    /**
     * Does nothing!
     */
    @Deprecated
    default boolean tryFill(ItemStack itemStack, int amount) {
        return false;
    }

    /**
     * Does nothing!
     */
    @Deprecated
    default boolean tryDrain(ItemStack itemStack, int amount) {
        return false;
    }
}
