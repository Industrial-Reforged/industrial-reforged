package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ItemUtils {
    // The corresponding rgb value: rgb(77,166,255)
    public static final int ENERGY_BAR_COLOR = 0x4DA6FF;
    public static final int HEAT_BAR_COLOR = 0xFF8000;

    public enum ChargeType {
        HEAT,
        FLUID,
        ENERGY,
    }

    public static int energyForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(ChargeType.ENERGY, itemStack)) * 13.0F));
    }

    public static int heatForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(ChargeType.HEAT, itemStack)) * 13.0F));
    }


    public static int fluidForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(ChargeType.FLUID, itemStack)) * 13.0F));
    }

    public static IEnergyItem getEnergyItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IEnergyItem energyItem) {
            return energyItem;
        }
        return null;
    }

    public static IHeatItem getHeatItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IHeatItem heatItem) {
            return heatItem;
        }
        return null;
    }

    public static IFluidItem getFluidItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IFluidItem fluidItem) {
            return fluidItem;
        }
        return null;
    }

    public static float getChargeRatio(ChargeType type, ItemStack stack) {
        return switch (type) {
            case HEAT -> (float) getHeatItem(stack).getHeatStored(stack) / getHeatItem(stack).getHeatCapacity();
            case FLUID -> (float) getFluidItem(stack).getFluidStored(stack) / getFluidItem(stack).getFluidCapacity();
            case ENERGY -> (float) getEnergyItem(stack).getEnergyStored(stack) / getEnergyItem(stack).getEnergyCapacity();
        };
    }

    public static ItemStack itemStackFromInteractionHand(InteractionHand interactionHand, Player player) {
        switch (interactionHand) {
            case MAIN_HAND -> {
                return player.getMainHandItem();
            }
            case OFF_HAND -> {
                return player.getOffhandItem();
            }
        }
        return ItemStack.EMPTY;
    }

}
