package com.indref.industrial_reforged.content.blocks.machines.primitive;

import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.misc.CustomFaucetInteractBlock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blocks.ContainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class WoodenBasinBlock extends ContainerBlock implements CustomFaucetInteractBlock {
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(2, 2, 0, 16, 6, 2),
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 2, 0, 2, 6, 14),
            Block.box(14, 2, 2, 16, 6, 16),
            Block.box(0, 2, 14, 14, 6, 16)
    ).reduce(Shapes::or).get();

    public WoodenBasinBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(properties1 -> new CastingBasinBlock(properties1, null));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends IRContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.WOODEN_BASIN.get();
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state != null ? state.setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection()) : null;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(blockEntity);
        IFluidHandler fluidHandler = CapabilityUtils.fluidHandlerCapability(blockEntity);
        if (itemHandler != null && fluidHandler != null) {
            IFluidHandler itemFluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
            if (itemFluidHandler != null && !itemFluidHandler.getFluidInTank(0).isEmpty()) {
                int drainAmount = fluidHandler.getTankCapacity(0) - fluidHandler.getFluidInTank(0).getAmount();
                FluidStack drained = itemFluidHandler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fluidHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
            } else if (itemFluidHandler != null) {
                int drainAmount = itemFluidHandler.getTankCapacity(0) - itemFluidHandler.getFluidInTank(0).getAmount();
                FluidStack drained = fluidHandler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                itemFluidHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
            } else if (itemStack.isEmpty()) {
                ItemStack removed = itemHandler.extractItem(0, itemHandler.getSlotLimit(0), false);
                if (!removed.isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(player, removed);
                }

            } else {
                ItemStack insertedItem = itemHandler.insertItem(0, itemStack.copy(), false);
                itemStack.setCount(insertedItem.getCount());
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockUtils.dropCastingScraps(level, pos);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public float getShapeMaxY(BlockGetter blockGetter, BlockPos blockPos) {
        return (float) 2 / 16;
    }
}
