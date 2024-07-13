package com.indref.industrial_reforged.registries.gui.menus;

import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.machines.BasicGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BasicGeneratorMenu extends IRAbstractContainerMenu<BasicGeneratorBlockEntity> {
    public BasicGeneratorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (BasicGeneratorBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public BasicGeneratorMenu(int pContainerId, Inventory inv, BasicGeneratorBlockEntity entity) {
        super(IRMenuTypes.BASIC_GENERATOR_MENU.get(), pContainerId, inv, entity);
        checkContainerSize(inv, 2);

        ItemStackHandler itemHandler = blockEntity.getItemHandler();

        this.addSlot(new SlotItemHandler(itemHandler, 0, 44, 37));
        this.addSlot(new SlotItemHandler(itemHandler, 1, 18, 37));

        addPlayerInventory(inv, 83 + 20);
        addPlayerHotbar(inv, 141 + 20);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                player, IRBlocks.BASIC_GENERATOR.get());
    }
}
