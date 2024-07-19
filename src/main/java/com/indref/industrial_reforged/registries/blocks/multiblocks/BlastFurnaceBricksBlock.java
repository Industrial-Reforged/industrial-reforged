package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlastFurnaceBricksBlock extends Block implements WrenchableBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlastFurnaceBricksBlock(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlastFurnaceMultiblock.BRICK_STATE, FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx)
                .setValue(FACING, Direction.NORTH)
                .setValue(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.UNFORMED);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            Optional<BlockPos> controllerPos = getControllerPos(pState, pLevel, pPos);
            controllerPos.ifPresent(pos -> MultiblockHelper.unform(IRMultiblocks.BLAST_FURNACE.get(), pos, pLevel));
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        Optional<BlockPos> controllerPos = getControllerPos(state, level, pos);
        if(controllerPos.isPresent() && state.getValue(BlastFurnaceMultiblock.BRICK_STATE) != BlastFurnaceMultiblock.BrickStates.UNFORMED) {
            BlockEntity blockEntity = level.getBlockEntity(controllerPos.get());
            if (blockEntity instanceof BlastFurnaceBlockEntity fakeBlockEntity && fakeBlockEntity.getActualBlockEntityPos().isPresent()) {
                BlockPos mainControllerPos = fakeBlockEntity.getActualBlockEntityPos().get();
                BlockEntity blastFurnaceBE = level.getBlockEntity(mainControllerPos);
                IFluidHandler fluidHandler = CapabilityUtils.fluidHandlerCapability(blastFurnaceBE);
                player.openMenu((BlastFurnaceBlockEntity) blastFurnaceBE, mainControllerPos);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }

    private Optional<BlockPos> getControllerPos(BlockState state, Level level, BlockPos pos) {
        Optional<BlockPos> controllerPos = Optional.empty();
        if (state.getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.TOP)) {
            controllerPos = Optional.of(pos.offset(0, -3, 0));
        } else {
            for (int i = 1; i < 3; i++) {
                BlockPos offsetPos = pos.offset(0, -i, 0);
                if (level.getBlockEntity(offsetPos) instanceof BlastFurnaceBlockEntity) {
                    controllerPos = Optional.of(offsetPos);
                }
            }
        }
        return controllerPos;
    }
}
