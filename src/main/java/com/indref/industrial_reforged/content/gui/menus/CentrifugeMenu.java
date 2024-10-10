package com.indref.industrial_reforged.content.gui.menus;

import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.content.blockentities.machines.CentrifugeBlockEntity;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

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
        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(blockEntity);
        // Centrifuge tanks
        // Input slot
        addSlot(new SlotItemHandler(itemHandler, 0, 80, 41));

        // Output tanks
        addSlot(new SlotItemHandler(itemHandler, 1, 80, 5));
        addSlot(new SlotItemHandler(itemHandler, 2, 116, 41));
        addSlot(new SlotItemHandler(itemHandler, 3, 80, 77));
        addSlot(new SlotItemHandler(itemHandler, 4, 44, 41));
        // Battery slot
        addSlot(new ChargingSlot(itemHandler, 5, ChargingSlot.ChargeMode.CHARGE, 9, 67));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, IRBlocks.CENTRIFUGE.get());
    }
}
