package com.indref.industrial_reforged.api.multiblocks;

import com.indref.industrial_reforged.api.blocks.Scannable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IMultiBlockController extends Scannable {
    IMultiblock getMultiblock();

    @Override
    default List<Component> displayText(BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        return null;
    }
}
