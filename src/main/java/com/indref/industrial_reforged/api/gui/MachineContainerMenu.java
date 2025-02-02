package com.indref.industrial_reforged.api.gui;

import com.indref.industrial_reforged.api.blockentities.container.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.blockentities.machine.MachineBlockEntity;
import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import org.jetbrains.annotations.NotNull;

public abstract class MachineContainerMenu<T extends ContainerBlockEntity> extends PDLAbstractContainerMenu<T> {
    public MachineContainerMenu(MenuType<?> menuType, int containerId, @NotNull Inventory inv, @NotNull T blockEntity) {
        super(menuType, containerId, inv, blockEntity);
    }

    @Override
    protected @NotNull Slot addSlot(@NotNull Slot slot) {
        if (slot instanceof ChargingSlot chargingSlot && this.blockEntity instanceof MachineBlockEntity machineBlockEntity) {
            machineBlockEntity.addBatterySlot(chargingSlot);
        }
        return super.addSlot(slot);
    }

}
