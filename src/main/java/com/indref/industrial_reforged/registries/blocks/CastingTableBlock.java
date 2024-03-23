package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CastingTableBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

import java.util.stream.Stream;

import static com.indref.industrial_reforged.registries.blockentities.multiblocks.CastingTableBlockEntity.CAST_SLOT;

@SuppressWarnings("deprecation")
public class CastingTableBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(0, 0, 0, 4, 10, 4),
            Block.box(12, 0, 0, 16, 10, 4),
            Block.box(12, 0, 12, 16, 10, 16),
            Block.box(0, 0, 12, 4, 10, 16),
            Block.box(0, 10, 0, 16, 13, 16)
    ).reduce(Shapes::or).get();

    public CastingTableBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CastingTableBlock::new);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CastingTableBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IItemHandler itemHandler = BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
        IFluidHandler fluidHandler = BlockUtils.getBlockEntityCapability(Capabilities.FluidHandler.BLOCK, blockEntity);
        if (!level.isClientSide()) {
            insertAndExtract(player, hand, itemHandler);
            fluidHandler.fill(new FluidStack(Fluids.LAVA, 1000), IFluidHandler.FluidAction.EXECUTE);
            blockEntity.setChanged();
        }
        IndustrialReforged.LOGGER.debug("Item: {}", BlockUtils.getClientItemHandler(blockPos).getStackInSlot(1));
        return InteractionResult.SUCCESS;
    }

    private static void insertAndExtract(Player player, InteractionHand interactionHand, IItemHandler itemHandler) {
        if (!player.getItemInHand(interactionHand).isEmpty()) {
            if (canInsert(itemHandler, player.getItemInHand(interactionHand))) {
                int count = player.getItemInHand(interactionHand).getCount();
                ItemStack itemStack = player.getItemInHand(interactionHand).copyAndClear();
                itemStack.setCount(count);
                itemHandler.insertItem(CAST_SLOT, itemStack, false);
            }
        } else if (player.getItemInHand(interactionHand).isEmpty()) {
            int extractIndex = getFirstForExtract(itemHandler);
            if (extractIndex != -1) {
                ItemStack stack = itemHandler.getStackInSlot(extractIndex);
                ItemHandlerHelper.giveItemToPlayer(player, stack.copy());
                itemHandler.extractItem(extractIndex, stack.getCount(), false);
            }
        }
    }

    // TODO: Remove loop and only insert into slot 0
    private static boolean canInsert(IItemHandler itemHandler, ItemStack toInsert) {
        return itemHandler.getStackInSlot(CAST_SLOT).isEmpty()
                || (itemHandler.getStackInSlot(CAST_SLOT).is(toInsert.getItem())
                && itemHandler.getStackInSlot(CAST_SLOT).getCount() + toInsert.getCount() <= toInsert.getMaxStackSize());
    }

    // TODO: Make slot 1 preferred for extraction
    private static int getFirstForExtract(IItemHandler itemHandler) {
        for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return createTickerHelper(p_153214_, IRBlockEntityTypes.CASTING_TABLE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(blockPos, blockState));
    }
}
