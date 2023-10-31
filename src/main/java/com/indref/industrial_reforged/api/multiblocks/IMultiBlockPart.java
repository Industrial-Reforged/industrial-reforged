package com.indref.industrial_reforged.api.multiblocks;

import com.indref.industrial_reforged.api.blocks.IScannable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IMultiBlockPart extends IScannable {
    @Override
    default List<Component> displayText(BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        return null;
    }
}
