package com.indref.industrial_reforged.registries.gui.menus;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.machines.CentrifugeBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CentrifugeMenu extends IRAbstractContainerMenu<CentrifugeBlockEntity> {
    private final Level level;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final Player player;

    public CentrifugeMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (CentrifugeBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(6));
    }

    public CentrifugeMenu(int containerId, Inventory inv, CentrifugeBlockEntity blockEntity, ContainerData data) {
        super(IRMenuTypes.CENTRIFUGE_MENU.get(), containerId, inv, blockEntity);
        this.level = inv.player.level();
        this.data = data;
        this.player = inv.player;
        this.access = ContainerLevelAccess.create(level, blockEntity.getBlockPos());
        addDataSlots(data);
        addPlayerInventory(inv, 83 + 20);
        addPlayerHotbar(inv, 141 + 20);
        Optional<ItemStackHandler> itemHandler = this.getBlockEntity().getItemHandler();
        if (itemHandler.isPresent()) {
            // Battery slot
            // addSlot(new SlotItemHandler(itemHandler.get(), 0, 9, 67));
            // Centrifuge slots
            addSlot(new SlotItemHandler(itemHandler.get(), 0, 80, 41));
            addSlot(new SlotItemHandler(itemHandler.get(), 1, 44, 41));
            addSlot(new SlotItemHandler(itemHandler.get(), 2, 116, 41));
            addSlot(new SlotItemHandler(itemHandler.get(), 3, 80, 5));
            addSlot(new SlotItemHandler(itemHandler.get(), 4, 80, 77));
        } else {
            IndustrialReforged.LOGGER.error("Centrifuge itemhandler does not exist. Was not able to add slots");
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, IRBlocks.CENTRIFUGE.get());
    }
}
