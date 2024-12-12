package com.indref.industrial_reforged.api.items.container;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public interface IFluidItem {
    static IFluidHandlerItem getFluidHandler(ItemStack itemStack) {
        return itemStack.getCapability(Capabilities.FluidHandler.ITEM);
    }

    default int getFluidStored(ItemStack itemStack) {
        IFluidHandlerItem fluidHandlerItem = getFluidHandler(itemStack);
        return fluidHandlerItem.getFluidInTank(0).getAmount();
    }

    int getFluidCapacity(ItemStack itemStack);
}
