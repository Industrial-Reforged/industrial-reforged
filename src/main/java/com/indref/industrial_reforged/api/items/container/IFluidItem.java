package com.indref.industrial_reforged.api.items.container;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public interface IFluidItem {
    default IFluidHandlerItem getFluidCap(ItemStack itemStack) {
        return itemStack.getCapability(Capabilities.FluidHandler.ITEM);
    }

    default void onFluidChanged(ItemStack itemStack, FluidStack oldFluid) {
    }

    int getDefaultFluidCapacity();
}
