package com.indref.industrial_reforged.util.recipes.crafting_station;

import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

public class ItemhandlerCraftingContainer implements CraftingContainer {
    private final NonNullList<ItemStack> items;
    private final AbstractContainerMenu menu;
    public final IItemHandler itemHandler;
    private final int width;
    private final int height;

    public ItemhandlerCraftingContainer(IItemHandler itemHandler, AbstractContainerMenu menu, int width, int height) {
        this.items = NonNullList.withSize(3 * 3, ItemStack.EMPTY);
        this.menu = menu;
        this.itemHandler = itemHandler;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        return p_18941_ >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(p_18941_);
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        ItemStack itemstack = ContainerHelper.removeItem(this.items, p_18942_, p_18943_);
        if (!itemstack.isEmpty()) {
            this.menu.slotsChanged(this);
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return ContainerHelper.takeItem(this.items, p_18951_);
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        this.items.set(p_18944_, p_18945_);
        this.menu.slotsChanged(this);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void fillStackedContents(StackedContents p_40281_) {
        for(ItemStack itemstack : this.items) {
            p_40281_.accountSimpleStack(itemstack);
        }
    }
}
