package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CastingBasinBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import static com.indref.industrial_reforged.registries.blockentities.multiblocks.CastingBasinBlockEntity.CAST_SLOT;

public class CastingBasinBlock extends ContainerBlock {
    private static final ConcurrentMap<Block, Block> ALTERNATE_VERSIONS = new ConcurrentHashMap<>();

    public static final VoxelShape SHAPE = Stream.of(
            Block.box(2, 2, 0, 16, 6, 2),
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 2, 0, 2, 6, 14),
            Block.box(14, 2, 2, 16, 6, 16),
            Block.box(0, 2, 14, 14, 6, 16)
    ).reduce(Shapes::or).get();

    public CastingBasinBlock(Properties p_49224_, Block ingredient) {
        super(p_49224_);
        if (ingredient != null) {
            ALTERNATE_VERSIONS.put(ingredient, this);
        }
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
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.CASTING_BASIN.get();
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
                ItemStack removed = itemHandler.extractItem(1, itemHandler.getStackInSlot(1).getMaxStackSize(), false);
                if (removed.isEmpty()) {
                    ItemStack removedCast = itemHandler.extractItem(0, itemHandler.getStackInSlot(0).getMaxStackSize(), false);
                    ItemHandlerHelper.giveItemToPlayer(player, removedCast, player.getInventory().selected);
                } else {
                    ItemHandlerHelper.giveItemToPlayer(player, removed);
                }
            } else {
                ItemStack insertedCastAmount = itemHandler.insertItem(0, itemStack.copy(), false);
                itemStack.setCount(insertedCastAmount.getCount());
            }
        }

        for (Block key : ALTERNATE_VERSIONS.keySet()) {
            Block val = ALTERNATE_VERSIONS.get(key);
            if (itemStack.is(key.asItem()) && !blockState.is(val)) {
                level.setBlockAndUpdate(blockPos, val.defaultBlockState());
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
}
