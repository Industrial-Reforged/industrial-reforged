package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.RotatableEntityBlock;
import com.indref.industrial_reforged.api.multiblocks.MultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlastFurnaceHatch extends RotatableEntityBlock implements MultiBlockController {
    public BlastFurnaceHatch(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Override
    public List<Component> displayOverlay(BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        return List.of();
    }

    @Override
    public List<Item> getCompatibleItems() {
        return List.of();
    }

    @Override
    public Multiblock getMultiblock() {
        return IRMultiblocks.BLAST_FURNACE.get();
    }
}
