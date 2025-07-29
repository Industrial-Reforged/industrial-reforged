package com.indref.industrial_reforged.util.machine;

import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.content.blockentities.machines.CentrifugeBlockEntity;
import net.minecraft.world.entity.player.Inventory;

@FunctionalInterface
public interface AltContainerFactory<B extends MachineBlockEntity, M extends MachineContainerMenu<B>> {
    M create(int containerId, Inventory inv, B blockEntity);
}
