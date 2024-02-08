package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.CastingTableBlockEntity;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CastingTableBlock extends BaseEntityBlock {
    public CastingTableBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CastingTableBlock::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(0, 0, 0, 16, 13, 16);
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
        if (!level.isClientSide()) {
            insertAndExtract(player, hand, itemHandler);
            blockEntity.setChanged();
        }
        IndustrialReforged.LOGGER.debug("Item: {}", BlockUtils.getClientItemHandler(blockPos).getStackInSlot(1));
        return InteractionResult.SUCCESS;
    }

    private static void insertAndExtract(Player player, InteractionHand interactionHand, IItemHandler itemHandler) {
        if (!player.getItemInHand(interactionHand).isEmpty()) {
            int insertIndex = getFirstForInsert(itemHandler, player.getItemInHand(interactionHand));
            if (insertIndex != -1) {
                itemHandler.insertItem(insertIndex, player.getItemInHand(interactionHand).copyAndClear(), false);
            }
        } else if (player.getItemInHand(interactionHand).isEmpty()) {
            int extractIndex = getFirstForExtract(itemHandler);
            if (extractIndex != -1) {
                ItemHandlerHelper.giveItemToPlayer(player, itemHandler.getStackInSlot(extractIndex).copyAndClear());
            }
        }
    }

    private static int getFirstForInsert(IItemHandler itemHandler, ItemStack toInsert) {
        for (int i = 0; i < itemHandler.getSlots() - 1; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty() || (itemHandler.getStackInSlot(i).is(toInsert.getItem()) && itemHandler.getStackInSlot(i).getCount() + toInsert.getCount() <= toInsert.getMaxStackSize())) {
                return i;
            }
        }
        return -1;
    }

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
