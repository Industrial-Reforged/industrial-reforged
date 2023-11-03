package com.indref.industrial_reforged.api.items.container;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public interface IFluidItem extends IContainerItem {
    Fluid getFluid();

    default IFluidHandlerItem getFluidHandler(ItemStack itemStack) {
        return itemStack.getCapability(Capabilities.FLUID_HANDLER_ITEM).orElseThrow(NullPointerException::new);
    }

    /**
     * Does nothing!
     */
    @Override
    @Deprecated
    default void setStored(ItemStack itemStack, int value) {
    }

    @Override
    default int getStored(ItemStack itemStack) {
        IFluidHandlerItem fluidHandlerItem = getFluidHandler(itemStack);
        return fluidHandlerItem.getFluidInTank(0).getAmount();
    }

    /**
     * @return true if was able to fill, false if wasn't able to do so
     */

    @Override
    default boolean tryFill(ItemStack itemStack, int amount) {
        return tryFill(getFluid(), amount, itemStack);
    }

    /**
     * @return true if was able to fill, false if wasn't able to do so
     */
    default boolean tryFill(Fluid fluid, int amount, ItemStack itemStack) {
        FluidStack fluidStack = new FluidStack(fluid, getStored(itemStack)+amount);
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
    @Override
    default boolean tryDrain(ItemStack itemStack, int amount) {
        return tryDrain(getFluid(), amount, itemStack);
    }

    /**
     * @return true if was able to fill, false if wasn't able to do so
     */
    default boolean tryDrain(Fluid fluid, int amount, ItemStack itemStack) {
        FluidStack fluidStack = new FluidStack(fluid, getStored(itemStack)-amount);
        IFluidHandlerItem fluidHandlerItem = getFluidHandler(itemStack);
        if (fluidHandlerItem.isFluidValid(0, fluidStack) && fluidHandlerItem.getFluidInTank(0).getAmount()-amount>=0) {
            fluidHandlerItem.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return false;
    }
}
