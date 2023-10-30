package com.indref.industrial_reforged.content.blocks;

import com.indref.industrial_reforged.api.items.IToolItem;
import com.indref.industrial_reforged.content.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.blockentities.PrimitiveForgeBE;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

public class PrimitiveForgeBlock extends BaseEntityBlock {
    private int progress = 0;

    public PrimitiveForgeBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PrimitiveForgeBE(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (player.getMainHandItem().getItem() instanceof IToolItem) {
            if (entity instanceof PrimitiveForgeBE primitiveForgeBE) {
                player.sendSystemMessage(Component.literal("Cur: "+progress));
                progress++;
                if (progress >= 3) {
                    primitiveForgeBE.craftItem();
                    progress = 0;
                }
                return InteractionResult.SUCCESS;
            }
        } else if (!player.getMainHandItem().equals(ItemStack.EMPTY)) {
            entity.getCapability(Capabilities.ITEM_HANDLER).ifPresent(itemHandler -> itemHandler.insertItem(0, player.getMainHandItem().copy(), false));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, IRBlockEntityTypes.PRIMITIVE_FORGE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

}
