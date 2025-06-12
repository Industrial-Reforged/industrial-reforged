package com.indref.industrial_reforged.content.gui.menus;

import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BlastFurnaceMenu extends MachineContainerMenu<BlastFurnaceBlockEntity> {

    public BlastFurnaceMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (BlastFurnaceBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public BlastFurnaceMenu(int pContainerId, Inventory inv, BlastFurnaceBlockEntity entity) {
        super(IRMenuTypes.BLAST_FURNACE_MENU.get(), pContainerId, inv, entity);
        checkContainerSize(inv, 2);

        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(entity);

        this.addSlot(new SlotItemHandler(itemHandler, 0, 18, 37));
        this.addSlot(new SlotItemHandler(itemHandler, 1, 44, 37));

        addPlayerHotbar(inv);
        addPlayerInventory(inv);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                player, IRBlocks.BLAST_FURNACE_HATCH.get());
    }

    @Override
    protected int getMergeableSlotCount() {
        return 2;
    }
}
