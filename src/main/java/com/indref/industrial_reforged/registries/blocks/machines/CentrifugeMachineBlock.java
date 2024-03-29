package com.indref.industrial_reforged.registries.blocks.machines;

import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.blockentities.machines.CentrifugeBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CentrifugeMachineBlock extends BaseEntityBlock {
    public CentrifugeMachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CentrifugeMachineBlock::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            player.openMenu((CentrifugeBlockEntity) level.getBlockEntity(blockPos), blockPos);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new CentrifugeBlockEntity(p_153215_, p_153216_);
    }
}
