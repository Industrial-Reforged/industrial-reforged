package com.indref.industrial_reforged.api.gui;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.machine.MachineBlockEntity;
import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class IRAbstractContainerMenu<T extends ContainerBlockEntity> extends AbstractContainerMenu {
    public final @NotNull T blockEntity;
    protected final @NotNull Inventory inv;
    protected int slotAmount;

    public @NotNull T getBlockEntity() {
        return blockEntity;
    }

    protected IRAbstractContainerMenu(MenuType<?> menuType, int containerId, @NotNull Inventory inv, @NotNull T blockEntity) {
        super(menuType, containerId);
        this.blockEntity = blockEntity;
        this.inv = inv;
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 83 + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 141));
        }
    }

    protected void addPlayerInventory(Inventory playerInventory, int y) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, y + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory, int y) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, y));
        }
    }

    @Override
    protected @NotNull Slot addSlot(@NotNull Slot slot) {
        if (slot instanceof ChargingSlot chargingSlot && this.blockEntity instanceof MachineBlockEntity machineBlockEntity) {
            machineBlockEntity.addBatterySlot(chargingSlot);
        }
        this.slotAmount++;
        return super.addSlot(slot);
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    @Override
    public @NotNull ItemStack quickMoveStack(Player playerIn, int pIndex) {
        try {
            Slot sourceSlot = slots.get(pIndex);

            if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM

            ItemStack sourceStack = sourceSlot.getItem();
            ItemStack copyOfSourceStack = sourceStack.copy();

            // Check if the slot clicked is one of the vanilla container slots
            int endIndex = TE_INVENTORY_FIRST_SLOT_INDEX + this.slotAmount - VANILLA_SLOT_COUNT;
            if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
                // This is a vanilla container slot so merge the stack into the tile inventory
                if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, endIndex, false)) {
                    return ItemStack.EMPTY;  // EMPTY_ITEM
                }
            } else if (pIndex < endIndex) {
                // This is a TE slot so merge the stack into the players inventory
                if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                IndustrialReforged.LOGGER.error("Invalid slotIndex: {}", pIndex);
                return ItemStack.EMPTY;
            }
            // If stack size == 0 (the entire stack was moved) set slot contents to null
            if (sourceStack.getCount() == 0) {
                sourceSlot.set(ItemStack.EMPTY);
            } else {
                sourceSlot.setChanged();
            }
            sourceSlot.onTake(playerIn, sourceStack);
            return copyOfSourceStack;
        } catch (Exception e) {
            IndustrialReforged.LOGGER.error("Encountered error: ", e);
        }
        return ItemStack.EMPTY;
    }

    public @NotNull Inventory getInv() {
        return inv;
    }
}
