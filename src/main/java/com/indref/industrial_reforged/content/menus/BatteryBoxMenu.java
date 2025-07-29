package com.indref.industrial_reforged.content.menus;

import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.content.blockentities.BatteryBoxBlockEntity;
import com.indref.industrial_reforged.registries.IRMachines;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.util.machine.IRMachine;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class BatteryBoxMenu extends MachineContainerMenu<BatteryBoxBlockEntity> {
    public BatteryBoxMenu(int containerId, @NotNull Inventory inv, @NotNull BatteryBoxBlockEntity blockEntity) {
        super(IRMenuTypes.BATTERY_BOX_MENU.get(), containerId, inv, blockEntity);
    }

    public BatteryBoxMenu(int containerId, @NotNull Inventory inv, RegistryFriendlyByteBuf byteBuf) {
        this(containerId, inv, (BatteryBoxBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

    @Override
    protected int getMergeableSlotCount() {
        return 0;
    }
}
