package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.items.container.IEnergyContainerItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {
    // The corresponding rgb value: rgb(77,166,255)
    public static final int ENERGY_BAR_COLOR = 0x4DA6FF;
    public static int energyForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(itemStack)) * 13.0F));
    }

    public static IEnergyContainerItem getEnergyItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IEnergyContainerItem energyItem) {
            return energyItem;
        }
        return null;
    }

    public static float getChargeRatio(ItemStack stack) {
        IEnergyContainerItem energyItem = getEnergyItem(stack);
        return (float) energyItem.getStored(stack) / energyItem.getCapacity(stack);
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
