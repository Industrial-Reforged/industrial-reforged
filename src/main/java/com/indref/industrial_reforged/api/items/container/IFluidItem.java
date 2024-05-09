package com.indref.industrial_reforged.api.items.container;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public interface IFluidItem {
    FluidStack getFluid();

    static IFluidHandlerItem getFluidHandler(ItemStack itemStack) {
        return itemStack.getCapability(Capabilities.FluidHandler.ITEM);
    }

    default int getFluidStored(ItemStack itemStack) {
        IFluidHandlerItem fluidHandlerItem = getFluidHandler(itemStack);
        return fluidHandlerItem.getFluidInTank(0).getAmount();
    }

    int getFluidCapacity(ItemStack itemStack);

    /**
     * @return true if was able to fill, false if wasn't able to do so
     */
    default boolean tryFillFluid(ItemStack itemStack, int amount) {
        return tryFillFluid(getFluid().getFluid(), amount, itemStack);
    }

    /**
     * @return true if was able to fill, false if wasn't able to do so
     */
    default boolean tryFillFluid(Fluid fluid, int amount, ItemStack itemStack) {
        FluidStack fluidStack = new FluidStack(fluid, getFluidStored(itemStack)+amount);
        IFluidHandlerItem fluidHandlerItem = getFluidHandler(itemStack);
        if (fluidHandlerItem.isFluidValid(0, fluidStack) && fluidHandlerItem.getFluidInTank(0).getAmount()+amount<=fluidHandlerItem.getTankCapacity(0)) {
            fluidHandlerItem.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return false;
    }

    /**
     * @return true if was able to fill, false if wasn't able to do so
     */
    default boolean tryDrainFluid(ItemStack itemStack, int amount) {
        return tryDrainFluid(getFluid().getFluid(), amount, itemStack);
    }

    /**
     * @return true if was able to fill, false if wasn't able to do so
     */
    default boolean tryDrainFluid(Fluid fluid, int amount, ItemStack itemStack) {
        FluidStack fluidStack = new FluidStack(fluid, getFluidStored(itemStack)-amount);
        IFluidHandlerItem fluidHandlerItem = getFluidHandler(itemStack);
        if (fluidHandlerItem.isFluidValid(0, fluidStack) && fluidHandlerItem.getFluidInTank(0).getAmount()-amount>=0) {
            fluidHandlerItem.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return false;
    }
}
