package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.capabilities.heat.IHeatStorage;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.LazyOptional;

public interface IHeatItem extends IContainerItem {
    static IHeatStorage getHeatStorage(ItemStack itemStack) {
        LazyOptional<IHeatStorage> cap = itemStack.getCapability(IRCapabilities.HEAT);
        if (cap.isPresent())
            return cap.orElseThrow(NullPointerException::new);

        return null;
    }

    @Override
    default void setStored(ItemStack itemStack, int value) {
        IHeatStorage heatStorage = getHeatStorage(itemStack);
        if (heatStorage != null) {
            heatStorage.setHeatStored(value);
        }
    }

    @Override
    default int getStored(ItemStack itemStack) {
        IHeatStorage heatStorage = getHeatStorage(itemStack);
        if (heatStorage != null) {
            return heatStorage.getHeatStored();
        }
        return -1;
    }

    /**
     * Does nothing!
     */
    @Override
    @Deprecated
    default boolean tryFill(ItemStack itemStack, int amount) {
        return false;
    }

    /**
     * Does nothing!
     */
    @Override
    @Deprecated
    default boolean tryDrain(ItemStack itemStack, int amount) {
        return false;
    }
}
