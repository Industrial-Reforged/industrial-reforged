package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class DisplayUtils {
    public static List<Component> displayEnergyInfo(BlockEntity blockEntity, BlockState blockState) {
        IEnergyBlock energyBlock;
        if (blockEntity instanceof IEnergyBlock energyBlock1)
            energyBlock = energyBlock1;
        else
            return List.of();

        return List.of(
                blockState.getBlock().getName().withStyle(ChatFormatting.WHITE),
                Component.translatable("scanner_info.energy_block.energy_ratio")
                        .withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(String.format("%d/%d", energyBlock.getEnergyStored(blockEntity), energyBlock.getEnergyCapacity())))
                        .withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(","))
                        .withStyle(ChatFormatting.WHITE)
        );
    }

    public static List<Component> displayHeatInfo(BlockEntity blockEntity, BlockState blockState) {
        IHeatBlock heatBlock;
        if (blockEntity instanceof IHeatBlock energyBlock1)
            heatBlock = energyBlock1;
        else
            return List.of();

        return List.of(
                blockState.getBlock().getName().withStyle(ChatFormatting.WHITE),
                Component.translatable("scanner_info.heat_block.heat_ratio")
                        .withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(String.format("%d/%d", heatBlock.getHeatStored(blockEntity), heatBlock.getHeatCapacity())))
                        .withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(","))
                        .withStyle(ChatFormatting.WHITE)
        );
    }
}
