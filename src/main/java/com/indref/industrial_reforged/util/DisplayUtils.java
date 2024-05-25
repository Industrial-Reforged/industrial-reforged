package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
                        .append(": ")
                        .append(Component.literal(String.format("%d/%d", energyBlock.getEnergyStored(), energyBlock.getEnergyCapacity())))
                        .append(Component.literal(","))
                        .withStyle(ChatFormatting.WHITE)
        );
    }

    public static List<Component> displayHeatInfo(BlockEntity blockEntity, BlockState blockState, MutableComponent name) {
        if (BlockUtils.isHeatBlock(blockEntity) && blockEntity instanceof IHeatBlock heatBlock)
            return List.of(
                    name.withStyle(ChatFormatting.WHITE),
                    Component.translatable("scanner_info.heat_block.heat_ratio")
                            .append(": ")
                            .append(Component.literal(String.format("%d/%d", heatBlock.getHeatStored(blockEntity), heatBlock.getHeatCapacity())))
                            .append(Component.literal(","))
                            .withStyle(ChatFormatting.WHITE)
            );

        return List.of();
    }
}
