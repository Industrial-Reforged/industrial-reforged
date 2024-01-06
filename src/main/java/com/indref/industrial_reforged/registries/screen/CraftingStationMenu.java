package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.api.screen.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.CraftingStationBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CraftingStationMenu extends IRAbstractContainerMenu {
    public final CraftingStationBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public CraftingStationMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public CraftingStationMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(IRMenuTypes.CRAFTING_STATION_MENU.get(), pContainerId, inv);
        checkContainerSize(inv, 1);
        blockEntity = ((CraftingStationBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        IItemHandler itemHandler = BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, blockEntity);

        addCraftingSlots(itemHandler);
        addStorageSlots(itemHandler);
        // Output slot
        this.addSlot(new SlotItemHandler(itemHandler, 27, 37, -16));
        addDataSlots(data);
        addPlayerHotbar(inv, 185);
        addPlayerInventory(inv, 127);
    }

    private void addCraftingSlots(IItemHandler itemHandler) {
        int x = 37;
        int y = 10;
        int index = 18;
        for (int yIndex = 0; yIndex < 3; yIndex++) {
            for (int xIndex = 0; xIndex < 3; xIndex++) {
                this.addSlot(new SlotItemHandler(itemHandler, index, x + xIndex * 18, y + yIndex * 18));
                index++;
            }
        }
    }

    private void addStorageSlots(IItemHandler itemHandler) {
        int x = 8;
        int y = 82;
        int index = 0;
        for (int yIndex = 0; yIndex < 2; yIndex++) {
            for (int xIndex = 0; xIndex < 9; xIndex++) {
                this.addSlot(new SlotItemHandler(itemHandler, index, x + xIndex * 18, y + yIndex * 18));
                index++;
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotId) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, IRBlocks.CRAFTING_STATION.get());
    }
}
