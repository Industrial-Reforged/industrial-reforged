package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.content.blockentities.CastingBasinBlockEntity;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.tags.CTags;
import com.indref.industrial_reforged.tags.IRTags;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public final class BlockUtils {
    public static BlockState rotateBlock(BlockState state, DirectionProperty prop, Comparable<?> currentValue) {
        List<Direction> directions = prop.getPossibleValues().stream().toList();
        int currentDirectionIndex = directions.indexOf(currentValue);
        int nextDirectionIndex = (currentDirectionIndex + 1) % directions.size();
        Direction nextDirection = directions.get(nextDirectionIndex);
        return state.setValue(prop, nextDirection);
    }

    public static BlockState stateForFacingPlacement(Block block, BlockPlaceContext ctx) {
        return block.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }

    public static StateDefinition.Builder<Block, BlockState> createFacingDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        return builder;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> T getBE(BlockGetter level, BlockPos blockPos, Class<T> clazz) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (clazz.isInstance(blockEntity)) {
            return (T) blockEntity;
        }
        return null;
    }

    public static void dropCastingScraps(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            IFluidHandler fluidHandler = CapabilityUtils.fluidHandler(blockEntity);
            if (fluidHandler != null) {
                for (int i = 0; i < fluidHandler.getTanks(); i++) {
                    FluidStack fluidInTank = fluidHandler.getFluidInTank(i);
                    if (!fluidInTank.isEmpty() && fluidInTank.getFluid().defaultFluidState().is(IRTags.Fluids.MOLTEN_METAL)) {
                        ItemStack stack = IRItems.CASTING_SCRAPS.toStack();
                        stack.set(IRDataComponents.SINGLE_FLUID, new SingleFluidStack(fluidInTank));
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                    }
                }
            }
        }
    }

}
