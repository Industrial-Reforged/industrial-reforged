package com.indref.industrial_reforged.api.blockentities;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBlockEntity extends IRContainerBlockEntity {
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
        IEnergyStorage energyStorage = getEuStorage();
        IEnergyStorage itemEnergyStorage = itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
        if (itemEnergyStorage != null && !level.isClientSide()) {
            if (batterySlot.getMode() == ChargingSlot.ChargeMode.CHARGE) {
                int filled = itemEnergyStorage.tryFillEnergy(Math.min(itemEnergyStorage.getMaxInput(), energyStorage.getMaxOutput()), true);
                int drained = energyStorage.tryDrainEnergy(filled, true);
                int newFilled = itemEnergyStorage.tryFillEnergy(drained, false);
                energyStorage.tryDrainEnergy(newFilled, false);
            } else {
                int drained = itemEnergyStorage.tryDrainEnergy(Math.min(itemEnergyStorage.getMaxOutput(), energyStorage.getMaxInput()), true);
                int filled = energyStorage.tryFillEnergy(drained, true);
                int newDrained = itemEnergyStorage.tryDrainEnergy(filled, false);
                energyStorage.tryFillEnergy(newDrained, false);
            }
        }
    }

}
