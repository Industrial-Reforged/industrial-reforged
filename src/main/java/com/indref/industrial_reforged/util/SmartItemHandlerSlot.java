package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

/**
 * A slot that works like the {@link net.neoforged.neoforge.items.SlotItemHandler}
 * but does not discard the container which means it still works for something like
 * crafting table recipes
 */
public class SmartItemHandlerSlot extends Slot {
    private final ItemhandlerCraftingContainer container;
    private final int slotIndex;
    private final int itemhandlerIndex;

    public SmartItemHandlerSlot(ItemhandlerCraftingContainer p_40223_, int slotIndex, int itemhandlerIndex, int x, int y) {
        super(p_40223_, slotIndex, x, y);
        this.container = p_40223_;
        this.slotIndex = slotIndex;
        this.itemhandlerIndex = itemhandlerIndex;
        // FIXME: SlotItemHandler creates an empty container. That's why slot changes don't get tracked
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (stack.isEmpty())
            return false;
        return container.itemHandler.isItemValid(itemhandlerIndex, stack);
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        return this.getItemHandler().getStackInSlot(itemhandlerIndex);
    }

    // Override if your IItemHandler does not implement IItemHandlerModifiable
    @Override
    public void set(@NotNull ItemStack stack) {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(itemhandlerIndex, stack);
        this.container.setItem(this.slotIndex, stack);
        this.setChanged();
    }

    public void initialize(ItemStack stack) {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(itemhandlerIndex, stack);
        this.container.setItem(slotIndex, stack);
        this.setChanged();
    }

    @Override
    public void onQuickCraft(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn) {

    }

    @Override
    public int getMaxStackSize() {
        return this.container.itemHandler.getSlotLimit(this.itemhandlerIndex);
    }

    @Override
    public void setChanged() {
        IndustrialReforged.LOGGER.debug("Changed slot content");
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        int maxInput = stack.getMaxStackSize();
        maxAdd.setCount(maxInput);

        IItemHandler handler = this.getItemHandler();
        ItemStack currentStack = handler.getStackInSlot(itemhandlerIndex);
        if (handler instanceof IItemHandlerModifiable) {
            IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;

            handlerModifiable.setStackInSlot(itemhandlerIndex, ItemStack.EMPTY);

            ItemStack remainder = handlerModifiable.insertItem(itemhandlerIndex, maxAdd, true);

            handlerModifiable.setStackInSlot(itemhandlerIndex, currentStack);

            return maxInput - remainder.getCount();
        } else {
            ItemStack remainder = handler.insertItem(itemhandlerIndex, maxAdd, true);

            int current = currentStack.getCount();
            int added = maxInput - remainder.getCount();
            return current + added;
        }
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.getItemHandler().extractItem(itemhandlerIndex, 1, true).isEmpty();
    }

    @Override
    @NotNull
    public ItemStack remove(int amount) {
        return this.getItemHandler().extractItem(itemhandlerIndex, amount, false);
    }

    public IItemHandler getItemHandler() {
        return container.itemHandler;
    }
}
