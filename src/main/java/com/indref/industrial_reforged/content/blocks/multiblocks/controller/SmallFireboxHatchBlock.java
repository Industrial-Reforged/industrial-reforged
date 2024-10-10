package com.indref.industrial_reforged.registries.blocks.multiblocks.controller;

import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.RotatableContainerBlock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.SmallFireboxBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.SmallFireboxMultiblock;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallFireboxHatchBlock extends RotatableContainerBlock {
    public SmallFireboxHatchBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(SmallFireboxMultiblock.FIREBOX_STATE, SmallFireboxMultiblock.FireboxState.UNFORMED));
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.SMALL_FIREBOX.get();
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

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {
        if(blockState.getValue(SmallFireboxMultiblock.FIREBOX_STATE) != SmallFireboxMultiblock.FireboxState.UNFORMED) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof SmallFireboxBlockEntity fakeBlockEntity) {
                BlockPos actualBlockEntityPos = fakeBlockEntity.getActualBlockEntityPos();
                if (actualBlockEntityPos != null) {
                    BlockEntity blastFurnaceBE = level.getBlockEntity(actualBlockEntityPos);
                    player.openMenu((SmallFireboxBlockEntity) blastFurnaceBE, actualBlockEntityPos);
                    return InteractionResult.SUCCESS;
                }
            }
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
}
