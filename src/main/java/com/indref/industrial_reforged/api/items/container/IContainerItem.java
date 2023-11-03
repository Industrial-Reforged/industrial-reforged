package com.indref.industrial_reforged.api.items.container;

import net.minecraft.world.item.ItemStack;

public interface IContainerItem {
    void setStored(ItemStack itemStack, int value);
    int getStored(ItemStack itemStack);
    int getCapacity();

    boolean tryDrain(ItemStack blockEntity, int amount);

    boolean tryFill(ItemStack blockEntity, int amount);
}
