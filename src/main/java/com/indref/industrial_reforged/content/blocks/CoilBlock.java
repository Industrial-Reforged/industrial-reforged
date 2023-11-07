package com.indref.industrial_reforged.content.blocks;

import com.indref.industrial_reforged.api.blocks.IWrenchable;
import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.content.blockentities.FireboxBlockEntity;
import com.indref.industrial_reforged.content.multiblocks.FireBoxMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CoilBlock extends BaseEntityBlock implements IMultiBlockController, IWrenchable {
    public CoilBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FireBoxMultiblock.FIREBOX_PART);
    }

    @Override
    public IMultiblock getMultiblock() {
        return new FireBoxMultiblock();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FireboxBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity fireBoxBlockEntity = level.getBlockEntity(blockPos);
        if (!blockState.getValue(FireBoxMultiblock.FIREBOX_PART).equals(FireBoxMultiblock.PartIndex.UNFORMED)) {
            NetworkHooks.openScreen(((ServerPlayer) player), (FireboxBlockEntity) fireBoxBlockEntity, blockPos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
