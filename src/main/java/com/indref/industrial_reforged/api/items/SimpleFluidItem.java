package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.items.container.IFluidItem;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleFluidItem extends ItemFluidContainer implements IFluidItem {
    public SimpleFluidItem(Properties properties, int capacity) {
        super(properties, capacity);
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 1;
    }

    public static class Colors implements ItemColor {
        @Override
        public int getColor(@NotNull ItemStack stack, int tintIndex) {
            if (tintIndex != 1) return 0xFFFFFFFF;
            IFluidHandlerItem cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseThrow(NullPointerException::new);
            FluidStack fluidStack = cap.getFluidInTank(1);
            if (fluidStack.getFluid() != Fluids.EMPTY) {
                return IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
            }
            return 0xFFFFFFFF;
        }
    }
}
