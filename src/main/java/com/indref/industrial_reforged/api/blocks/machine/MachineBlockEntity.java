package com.indref.industrial_reforged.api.blocks.machine;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import com.indref.industrial_reforged.util.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBlockEntity extends ContainerBlockEntity {
    private @Nullable ChargingSlot batterySlot;

    public MachineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void addBatterySlot(ChargingSlot chargingSlot) {
        this.batterySlot = chargingSlot;
    }

    @Override
    public void commonTick() {
        super.commonTick();
        if (this.batterySlot != null) {
            tickBatterySlot();
        }
    }

    private void tickBatterySlot() {
        ItemStack itemStack = this.batterySlot.getItem();
        IEnergyStorage energyStorage = CapabilityUtils.energyStorageCapability(this);
        IEnergyStorage itemEnergyStorage = itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
        if (itemEnergyStorage != null) {
            int filled = itemEnergyStorage.tryFillEnergy(Math.min(itemEnergyStorage.getMaxInput(), energyStorage.getMaxOutput()), true);
            int drained = energyStorage.tryDrainEnergy(filled, true);
            int newFilled = itemEnergyStorage.tryFillEnergy(drained, false);
            energyStorage.tryDrainEnergy(newFilled, false);
        }
    }
}
