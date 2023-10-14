package com.indref.industrial_reforged.api.items.container;

import net.minecraft.world.item.ItemStack;

public interface IContainerItem {
    int getStored(ItemStack itemStack);
    int getCapacity(ItemStack itemStack);
}
