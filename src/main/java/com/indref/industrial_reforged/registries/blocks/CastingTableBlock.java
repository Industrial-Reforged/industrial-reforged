package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.blockentities.CastingTableBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Util;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CastingTableBlock extends BaseEntityBlock {
    public CastingTableBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CastingTableBlock::new);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Stream.of(
                Block.box(0, 0, 0, 4, 10, 2),
                Block.box(0, 0, 2, 2, 10, 4),
                Block.box(12, 0, 0, 14, 10, 2),
                Block.box(14, 0, 12, 16, 10, 14),
                Block.box(2, 0, 14, 4, 10, 16),
                Block.box(0, 0, 12, 2, 10, 16),
                Block.box(12, 0, 14, 16, 10, 16),
                Block.box(0, 10, 14, 16, 13, 16),
                Block.box(0, 10, 0, 16, 13, 2),
                Block.box(0, 10, 2, 2, 13, 14),
                Block.box(14, 10, 2, 16, 13, 14),
                Block.box(2, 10, 2, 14, 12, 14),
                Block.box(14, 0, 0, 16, 10, 4)
        ).reduce(Shapes::or).get();
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult p_60508_) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IItemHandler itemHandler = BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
        itemHandler.insertItem(0, player.getItemInHand(hand), false);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CastingTableBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }
}
