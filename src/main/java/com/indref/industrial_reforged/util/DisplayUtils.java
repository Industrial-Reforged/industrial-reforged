package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class DisplayUtils {
    public static void displayEnergyInfo(List<Component> displayText, BlockEntity blockEntity, BlockState blockState) {
        IEnergyStorage energyStorage = CapabilityUtils.blockEntityCapability(IRCapabilities.EnergyStorage.BLOCK, blockEntity);

        if (energyStorage == null) return;

        displayText.add(
                blockState.getBlock().getName().withStyle(ChatFormatting.WHITE)
        );
        displayText.add(
                Component.translatable("scanner_info.energy_block.energy_ratio")
                        .append(": ")
                        .append(Component.literal(String.format("%d/%d", energyStorage.getEnergyStored(), energyStorage.getEnergyCapacity())))
                        .append(Component.literal(","))
                        .withStyle(ChatFormatting.WHITE)
        );
    }

    public static List<Component> displayHeatInfo(BlockEntity blockEntity, BlockState blockState, MutableComponent name) {
        IHeatStorage heatStorage = CapabilityUtils.heatStorageCapability(blockEntity);
        if (heatStorage != null) {
            return List.of(
                    name.withStyle(ChatFormatting.WHITE),
                    Component.translatable("scanner_info.heat_block.heat_ratio")
                            .append(": ")
                            .append(Component.literal(String.format("%d/%d", heatStorage.getHeatStored(), heatStorage.getHeatCapacity())))
                            .append(Component.literal(","))
                            .withStyle(ChatFormatting.WHITE)
            );
        }

        return List.of();
    }
}
