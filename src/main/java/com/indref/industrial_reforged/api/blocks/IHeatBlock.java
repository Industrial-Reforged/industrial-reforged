package com.indref.industrial_reforged.api.blocks;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IHeatBlock extends IScannable {
    EnergyStorageProvider getHeatStorage();

    default int getHeatStored(BlockEntity blockEntity) {
        IHeatStorage heatStorage = blockEntity.getCapability(IRCapabilities.HEAT).orElseThrow(NullPointerException::new);
        return heatStorage.getHeatStored();
    }

    default int getHeatCapacity(BlockEntity blockEntity) {
        IHeatStorage heatStorage = blockEntity.getCapability(IRCapabilities.HEAT).orElseThrow(NullPointerException::new);
        return heatStorage.getHeatCapacity();
    }

    @Override
    default List<Component> displayText(BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        IHeatBlock heatBlock = null;
        BlockEntity blockEntity = level.getBlockEntity(scannedBlockPos);
        if (blockEntity instanceof IHeatBlock energyBlock1)
            heatBlock = energyBlock1;

        return List.of(
                scannedBlock.getBlock().getName(),
                MutableComponent.create(ComponentContents.EMPTY)
                        .append(Component.translatable("scanner_info.heat_block.heat_ratio"))
                        .append(Component.literal(String.format("%d/%d", heatBlock.getHeatStored(blockEntity), heatBlock.getHeatCapacity(blockEntity))))
                        .append(Component.literal(",")),
                Component.literal(String.valueOf(heatBlock.getHeatStored(blockEntity)))
        );
    }
}
