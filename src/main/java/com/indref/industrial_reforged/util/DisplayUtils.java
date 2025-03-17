package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.translations.IRTranslations;
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
                IRTranslations.General.ENERGY_NAME.component()
                        .append(": ")
                        .append(IRTranslations.Tooltip.ENERGY_AMOUNT_WITH_CAPACITY.component(energyStorage.getEnergyStored(), energyStorage.getEnergyCapacity()))
                        .append(IRTranslations.General.ENERGY_UNIT.component())
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
                IRTranslations.General.HEAT_NAME.component()
                        .append(": ")
                        .append(IRTranslations.Tooltip.HEAT_AMOUNT_WITH_CAPACITY.component(heatStorage.getHeatStored(), heatStorage.getHeatCapacity()))
                        .append(IRTranslations.General.HEAT_UNIT.component())
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
                    IRTranslations.General.FLUID_NAME.component()
                            .append(": ")
                            .append(IRTranslations.Tooltip.FLUID_AMOUNT_WITH_CAPACITY.component(fluidHandler.getFluidInTank(i).getAmount(), fluidHandler.getTankCapacity(i)))
                            .append(IRTranslations.General.FLUID_UNIT.component())
                            .withStyle(ChatFormatting.WHITE)
            );
        }
    }
}
