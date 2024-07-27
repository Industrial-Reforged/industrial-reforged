package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.CanAttachFaucetBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.multiblocks.FakeBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BlastFurnaceHatchBlock extends ContainerBlock implements CanAttachFaucetBlock {
    public BlastFurnaceHatchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.BLAST_FURNACE.get();
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
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        if (!blockState.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.UNFORMED)) {
            MultiblockHelper.unform(IRMultiblocks.BLAST_FURNACE.get(), blockPos, level);
        }

        super.onRemove(blockState, level, blockPos, newState, p_60519_);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {
        if (blockState.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.FORMED)) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BlastFurnaceBlockEntity fakeBlockEntity && fakeBlockEntity.getActualBlockEntityPos().isPresent()) {
                BlockPos mainControllerPos = fakeBlockEntity.getActualBlockEntityPos().get();
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
