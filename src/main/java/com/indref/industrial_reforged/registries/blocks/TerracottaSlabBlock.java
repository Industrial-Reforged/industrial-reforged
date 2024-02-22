package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.api.multiblocks.MultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TerracottaSlabBlock extends SlabBlock implements MultiBlockController {
    public TerracottaSlabBlock(Properties p_56359_) {
        super(p_56359_);
    }

    @Override
    public Multiblock getMultiblock() {
        return IRMultiblocks.CRUCIBLE_CERAMIC.get();
    }

    @Override
    public List<Component> displayOverlay(BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        return List.of();
    }

    @Override
    public List<Item> getCompatibleItems() {
        return List.of();
    }
}
