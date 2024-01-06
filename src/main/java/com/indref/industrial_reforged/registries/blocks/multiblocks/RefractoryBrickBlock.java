package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.FireboxBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.FireBoxMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.NetworkHooks;

public class RefractoryBrickBlock extends Block implements Wrenchable {
    public RefractoryBrickBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState ignored, boolean p_60519_) {
        if (!blockState.getValue(FireBoxMultiblock.FIREBOX_PART).equals(FireBoxMultiblock.PartIndex.UNFORMED)) {
            MultiblockHelper.unform(IRMultiblocks.FIREBOX_REFRACTORY.get(), blockPos, level);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide() && !blockState.getValue(FireBoxMultiblock.FIREBOX_PART).equals(FireBoxMultiblock.PartIndex.UNFORMED))
            for (BlockPos pos : BlockUtils.getBlocksAroundSelf3x3(blockPos)) {
                BlockEntity fireBoxBlockEntity = level.getBlockEntity(pos);
                if (fireBoxBlockEntity instanceof FireboxBlockEntity) {
                    NetworkHooks.openScreen(((ServerPlayer) player), (FireboxBlockEntity) fireBoxBlockEntity, pos);
                }
            }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FireBoxMultiblock.FIREBOX_PART);
    }
}
