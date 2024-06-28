package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SimpleFluidItem extends Item implements IFluidItem {
    private final int capacity;
    public SimpleFluidItem(Properties properties, int capacity) {
        super(properties.component(IRDataComponents.FLUID.get(), SimpleFluidContent.EMPTY));
        this.capacity = capacity;
    }

    @Override
    public int getFluidCapacity(ItemStack itemStack) {
        return this.capacity;
    }


    @Override
    public int getUseDuration(ItemStack stack, LivingEntity p_344979_) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
        ItemUtils.addFluidToolTip(p_41423_, p_41421_);
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
