package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.RotatableEntityBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.multiblocks.SmallFireboxMultiblock;
import com.indref.industrial_reforged.util.MultiblockUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmallFireboxHatchBlock extends RotatableEntityBlock {
    public SmallFireboxHatchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return super.getStateForPlacement(p_49820_).setValue(SmallFireboxMultiblock.FIREBOX_STATE, SmallFireboxMultiblock.FireboxState.UNFORMED);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(SmallFireboxHatchBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(SmallFireboxMultiblock.FIREBOX_STATE));
    }
}
