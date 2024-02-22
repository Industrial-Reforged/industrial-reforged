package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.util.MultiblockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record BlastFurnaceMultiblock() implements Multiblock {
    @Override
    public Block getController() {
        return IRBlocks.BLAST_FURNACE_HATCH.get();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return MultiblockUtils.singleBlockMultiblock(List.of(
                0,
                0,
                1
        ));
    }

    @Override
    public Map<Integer, @Nullable Block> getDefinition() {
        return Map.of(0, IRBlocks.BLAST_FURNACE_BRICKS.get(), 1, IRBlocks.BLAST_FURNACE_HATCH.get());
    }

    @Override
    public void formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY) {

    }

    @Override
    public void unformBlock(Level level, BlockPos blockPos, BlockPos controllerPos) {

    }
}
