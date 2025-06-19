package com.indref.industrial_reforged.content.gui.menus;

import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.content.blockentities.generators.BasicGeneratorBlockEntity;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BasicGeneratorMenu extends MachineContainerMenu<BasicGeneratorBlockEntity> {
    public BasicGeneratorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (BasicGeneratorBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public BasicGeneratorMenu(int pContainerId, Inventory inv, BasicGeneratorBlockEntity entity) {
        super(IRMenuTypes.BASIC_GENERATOR_MENU.get(), pContainerId, inv, entity);
        checkContainerSize(inv, 2);

        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(entity);

        this.addSlot(new SlotItemHandler(itemHandler, 0, 80, 54));
        this.addSlot(new ChargingSlot(itemHandler, 1, ChargingSlot.ChargeMode.CHARGE, 9, 68));

        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                player, IRBlocks.BASIC_GENERATOR.get());
    }

    @Override
    protected int getMergeableSlotCount() {
        return 2;
    }
}
