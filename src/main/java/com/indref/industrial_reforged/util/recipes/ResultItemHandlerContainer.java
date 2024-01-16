package com.indref.industrial_reforged.util.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

public class ResultItemHandlerContainer implements Container, RecipeCraftingHolder {
    @Nullable
    private RecipeHolder<?> recipeUsed;
    private final IItemHandler itemHandler;
    private final int index;

    public ResultItemHandlerContainer(IItemHandler itemHandler, int index) {
        this.itemHandler = itemHandler;
        this.index = index;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack itemStack = itemHandler.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int p_40147_) {
        return this.itemHandler.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index1, int amount) {
        return itemHandler.extractItem(index, itemHandler.getStackInSlot(index).getCount(), false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_40160_) {
        return itemHandler.extractItem(index, itemHandler.getStackInSlot(index).getCount(), false);
    }

    @Override
    public void setItem(int p_40152_, ItemStack p_40153_) {
        ((IItemHandlerModifiable) itemHandler).setStackInSlot(index, p_40153_);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player p_40155_) {
        return true;
    }

    @Override
    public void clearContent() {
        ((IItemHandlerModifiable) itemHandler).setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> p_301012_) {
        this.recipeUsed = p_301012_;
    }

    @Nullable
    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return this.recipeUsed;
    }
}
