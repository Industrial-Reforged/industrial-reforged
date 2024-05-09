package com.indref.industrial_reforged.api.items.container;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public interface IItemItem {
    private static IItemHandler getCap(ItemStack itemStack) {
        return itemStack.getCapability(Capabilities.ItemHandler.ITEM);
    }

    int getSlots(ItemStack itemStack);

    default List<ItemStack> getItems(ItemStack itemStack) {
        IItemHandler itemHandler = getCap(itemStack);
        List<ItemStack> stacks = new ArrayList<>();
        int slots = itemHandler.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                stacks.add(stackInSlot);
            }
        }
        return stacks;
    }

    default ItemStack get(ItemStack itemStack, int slot) {
        return getCap(itemStack).getStackInSlot(slot);
    }

    default void clearItems(ItemStack itemStack) {
        getItems(itemStack).clear();
    }

    default void insert(ItemStack itemStack, int slot, ItemStack toInsert) {
        getCap(itemStack).insertItem(slot, toInsert, false);
    }

    default void extract(ItemStack itemStack, int slot, int amount) {
        getCap(itemStack).extractItem(slot, amount, false);
    }
}
