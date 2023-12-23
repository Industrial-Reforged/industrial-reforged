package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.items.container.IFluidItem;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleFluidItem extends Item implements IFluidItem {
    private final int capacity;
    public SimpleFluidItem(Properties properties, int capacity) {
        super(properties);
        this.capacity = capacity;
    }

    @Override
    public int getFluidCapacity() {
        return this.capacity;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 1;
    }

    public static class Colors implements ItemColor {
        @Override
        public int getColor(@NotNull ItemStack stack, int tintIndex) {
            if (tintIndex != 1) return 0xFFFFFFFF;
            IFluidHandlerItem cap = stack.getCapability(Capabilities.FluidHandler.ITEM);
            FluidStack fluidStack = cap.getFluidInTank(1);
            if (fluidStack.getFluid() != Fluids.EMPTY) {
                return IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
            }
            return 0xFFFFFFFF;
        }
    }
}
