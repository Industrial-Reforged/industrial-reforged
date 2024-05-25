package com.indref.industrial_reforged.registries.blocks.machines;

import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.machines.CentrifugeBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CentrifugeBlock extends BaseEntityBlock implements Wrenchable {
    public CentrifugeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CentrifugeBlock::new);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState p_60503_, Level level, BlockPos blockPos, Player player, BlockHitResult p_60508_) {
        if (!level.isClientSide()) {
            player.openMenu((CentrifugeBlockEntity) level.getBlockEntity(blockPos), blockPos);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) return null;

        return createTickerHelper(pBlockEntityType, IRBlockEntityTypes.CENTRIFUGE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new CentrifugeBlockEntity(p_153215_, p_153216_);
    }
}
