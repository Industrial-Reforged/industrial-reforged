package com.indref.industrial_reforged.content.blocks.multiblocks.controller;

import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.misc.CanAttachFaucetBlock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableContainerBlock;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BlastFurnaceHatchBlock extends RotatableContainerBlock implements CanAttachFaucetBlock {
    public BlastFurnaceHatchBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BlastFurnaceMultiblock.ACTIVE, false));
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends IRContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.BLAST_FURNACE.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BlastFurnaceHatchBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(BlastFurnaceMultiblock.BRICK_STATE).add(BlastFurnaceMultiblock.ACTIVE));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(BlastFurnaceMultiblock.ACTIVE) && state.getValue(BlastFurnaceMultiblock.BRICK_STATE) == BlastFurnaceMultiblock.BrickStates.FORMED ? 10 : 0;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        if (!blockState.is(newState.getBlock())) {
            if (!blockState.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.UNFORMED)) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity) {
                    BlockPos actualBlockEntityPos = blastFurnaceBlockEntity.getActualBlockEntityPos();
                    if (actualBlockEntityPos != null) {
                        MultiblockHelper.unform(IRMultiblocks.BLAST_FURNACE.get(), actualBlockEntityPos, level);
                        BlockUtils.dropCastingScraps(level, blockPos);
                    }
                }
            }
        }

        super.onRemove(blockState, level, blockPos, newState, p_60519_);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {
        if (blockState.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.FORMED)) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BlastFurnaceBlockEntity fakeBlockEntity && fakeBlockEntity.getActualBlockEntityPos() != null) {
                BlockPos mainControllerPos = fakeBlockEntity.getActualBlockEntityPos();
                BlockEntity blastFurnaceBE = level.getBlockEntity(mainControllerPos);
                player.openMenu((BlastFurnaceBlockEntity) blastFurnaceBE, mainControllerPos);
                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.FAIL;
    }

    @Override
    public boolean canAttachFaucetToBlock(BlockState blockState, BlockPos blockPos, Level level, Direction facing) {
        return blockState.getValue(BlastFurnaceMultiblock.BRICK_STATE) == BlastFurnaceMultiblock.BrickStates.FORMED;
    }
}
