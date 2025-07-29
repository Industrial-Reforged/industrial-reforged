package com.indref.industrial_reforged.api.gui;

import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import com.indref.industrial_reforged.api.gui.slots.SlotAccessor;
import com.indref.industrial_reforged.api.gui.slots.UpgradeSlot;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class MachineContainerMenu<T extends MachineBlockEntity> extends PDLAbstractContainerMenu<T> {
    private final List<UpgradeSlot> upgradeSlots;

    public MachineContainerMenu(MenuType<?> menuType, int containerId, @NotNull Inventory inv, @NotNull T blockEntity) {
        super(menuType, containerId, inv, blockEntity);
        this.upgradeSlots = new ArrayList<>();
        if (blockEntity.supportsUpgrades()) {
            for (int i = 0; i < blockEntity.getUpgradeItemHandler().getSlots(); i++) {
                UpgradeSlot slot = new UpgradeSlot(blockEntity.getUpgradeItemHandler(), i, 179, 51 + i * 20);
                slot.setActive(false);
                this.addSlot(slot);
                this.upgradeSlots.add(slot);
            }
        }
    }

    public List<UpgradeSlot> getUpgradeSlots() {
        return upgradeSlots;
    }

    @Override
    protected @NotNull Slot addSlot(@NotNull Slot slot) {
        if (slot instanceof ChargingSlot chargingSlot && this.blockEntity instanceof MachineBlockEntity machineBlockEntity) {
            machineBlockEntity.addBatterySlot(chargingSlot);
        }
        return super.addSlot(slot);
    }

    public void setUpgradeSlotPositions(int startY) {
        List<UpgradeSlot> upgradeSlots = this.getUpgradeSlots();
        for (int i = 0; i < upgradeSlots.size(); i++) {
            UpgradeSlot upgradeSlot = upgradeSlots.get(i);
            ((SlotAccessor) upgradeSlot).setY(startY + i * 20);
        }
    }

}
