package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public final class DisplayUtils {
    public static void displayEnergyInfo(List<Component> displayText, BlockState scannedBlock, BlockPos blockPos, Level level) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IEnergyStorage energyStorage = CapabilityUtils.energyStorageCapability(blockEntity);

        if (energyStorage == null) return;

        displayText.add(
                scannedBlock.getBlock().getName().withStyle(ChatFormatting.WHITE)
        );
        displayText.add(
                Component.translatable("scanner_info.energy_block.energy_ratio")
                        .append(": ")
                        .append(Component.literal(String.format("%d/%d", energyStorage.getEnergyStored(), energyStorage.getEnergyCapacity())))
                        .append(Component.literal(","))
                        .withStyle(ChatFormatting.WHITE)
        );
    }

    public static void displayHeatInfo(List<Component> displayText, BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        BlockEntity blockEntity = level.getBlockEntity(scannedBlockPos);
        IHeatStorage heatStorage = CapabilityUtils.heatStorageCapability(blockEntity);

        if (heatStorage == null) return;

        displayText.add(
                scannedBlock.getBlock().getName().withStyle(ChatFormatting.WHITE)
        );
        displayText.add(
                Component.translatable("scanner_info.heat_block.heat_ratio")
                        .append(": ")
                        .append(Component.literal(String.format("%d/%d", heatStorage.getHeatStored(), heatStorage.getHeatCapacity())))
                        .append(Component.literal(","))
                        .withStyle(ChatFormatting.WHITE)
        );
    }

    public static void displayFluidInfo(List<Component> displayText, BlockState scannedBlock, BlockPos blockPos, Level level) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IFluidHandler fluidHandler = CapabilityUtils.fluidHandlerCapability(blockEntity);

        if (fluidHandler == null) return;

        displayText.add(
                scannedBlock.getBlock().getName().withStyle(ChatFormatting.WHITE)
        );
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            displayText.add(
                    Component.translatable("scanner_info.heat_block.heat_ratio")
                            .append(": ")
                            .append(Component.literal(String.format("%d/%d", fluidHandler.getFluidInTank(i).getAmount(), fluidHandler.getTankCapacity(i))))
                            .append(Component.literal(","))
                            .withStyle(ChatFormatting.WHITE)
            );
        }
    }
}
