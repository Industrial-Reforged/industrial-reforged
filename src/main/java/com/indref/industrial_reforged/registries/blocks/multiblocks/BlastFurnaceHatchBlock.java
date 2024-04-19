package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class BlastFurnaceHatchBlock extends BaseEntityBlock implements Wrenchable {
    public BlastFurnaceHatchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BlastFurnaceHatchBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(BlastFurnaceMultiblock.BRICK_STATE));
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        if (!blockState.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.UNFORMED)) {
            IndustrialReforged.LOGGER.debug("formed, now unforming");
            MultiblockHelper.unform(IRMultiblocks.BLAST_FURNACE.get(), blockPos, level);
        }

        super.onRemove(blockState, level, blockPos, newState, p_60519_);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pState.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.FORMED)) {
            BlockUtils.blockEntityAt(pLevel, pPos).ifPresent(be -> {
                if (be instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity) {
                    if (blastFurnaceBlockEntity.getActualBlockEntity().isPresent()
                            && blastFurnaceBlockEntity.getActualBlockEntity().get() instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity1) {
                        Utils.openMenu(pPlayer, blastFurnaceBlockEntity1);
                    }
                }
            });
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlastFurnaceBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;

        return createTickerHelper(blockEntityType, IRBlockEntityTypes.BLAST_FURNACE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick());
    }
}
