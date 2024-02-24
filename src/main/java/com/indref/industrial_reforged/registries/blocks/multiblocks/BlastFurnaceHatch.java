package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.RotatableEntityBlock;
import com.indref.industrial_reforged.api.multiblocks.MultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_);
        p_49915_.add(BlastFurnaceMultiblock.BRICK_STATE);
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

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if (p_60506_.isShiftKeyDown()) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.HATCH_LEFT));
        } else {
            level.setBlockAndUpdate(blockPos, blockState.setValue(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.HATCH_RIGHT));
        }
        return InteractionResult.SUCCESS;
    }
}
