package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.RotatableEntityBlock;
import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.SmallFireboxBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.SmallFireboxMultiblock;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallFireboxHatchBlock extends RotatableEntityBlock implements Wrenchable {
    public SmallFireboxHatchBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(SmallFireboxMultiblock.FIREBOX_STATE, SmallFireboxMultiblock.FireboxState.UNFORMED));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(SmallFireboxMultiblock.FIREBOX_STATE));
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
        return new SmallFireboxBlockEntity(blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockState.getValue(SmallFireboxMultiblock.FIREBOX_STATE).equals(SmallFireboxMultiblock.FireboxState.FORMED)
                && blockEntity instanceof SmallFireboxBlockEntity fireboxBlockEntity) {
            Utils.openMenu(player, fireboxBlockEntity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            MultiblockHelper.unform(IRMultiblocks.SMALL_FIREBOX.get(), pPos, pLevel);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) return null;

        return createTickerHelper(pBlockEntityType, IRBlockEntityTypes.SMALL_FIREBOX.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }
}
