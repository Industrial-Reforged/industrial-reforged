package com.indref.industrial_reforged.api.capabilities.item;

import com.indref.industrial_reforged.util.Utils;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record SidedItemHandler(IItemHandler innerHandler,
                               IOAction action,
                               IntList slots) implements IItemHandler {
    public SidedItemHandler(IItemHandler innerHandler, Pair<IOAction, int[]> actionSlotsPair) {
        this(innerHandler, actionSlotsPair != null ? actionSlotsPair.left() : IOAction.NONE, actionSlotsPair != null ? Utils.intArrayToList(actionSlotsPair.right()) : IntList.of());
    }

    @Override
    public int getSlots() {
        return innerHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        return innerHandler.getStackInSlot(i);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean simulate) {
        return action == IOAction.INSERT || action == IOAction.BOTH && slots.contains(slot) ? innerHandler.insertItem(slot, itemStack, simulate) : itemStack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return action == IOAction.EXTRACT || action == IOAction.BOTH && slots.contains(slot) ? innerHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int i) {
        return innerHandler.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack itemStack) {
        return action == IOAction.INSERT || action == IOAction.BOTH && slots.contains(slot) && innerHandler.isItemValid(slot, itemStack);
    }
}
