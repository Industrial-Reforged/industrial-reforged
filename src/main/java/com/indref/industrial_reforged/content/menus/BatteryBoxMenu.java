package com.indref.industrial_reforged.content.menus;

import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import com.indref.industrial_reforged.content.blockentities.BatteryBoxBlockEntity;
import com.indref.industrial_reforged.registries.IRMachines;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.util.machine.IRMachine;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BatteryBoxMenu extends MachineContainerMenu<BatteryBoxBlockEntity> {
    public BatteryBoxMenu(int containerId, @NotNull Inventory inv, @NotNull BatteryBoxBlockEntity blockEntity) {
        super(IRMachines.BATTERY_BOX.getMenuType(), containerId, inv, blockEntity);

        IItemHandler itemHandler = blockEntity.getItemHandler();
        addSlot(new ChargingSlot(itemHandler, 0, ChargingSlot.ChargeMode.DECHARGE, 39, 43));
        addSlot(new ChargingSlot(itemHandler, 1, ChargingSlot.ChargeMode.CHARGE, 176 - 36 - 19, 43));
        addPlayerInventory(inv, 83 + 21);
        addPlayerHotbar(inv, 141 + 21);

    }

    public BatteryBoxMenu(int containerId, @NotNull Inventory inv, RegistryFriendlyByteBuf byteBuf) {
        this(containerId, inv, (BatteryBoxBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

    @Override
    protected int getMergeableSlotCount() {
        return 2;
    }
}
