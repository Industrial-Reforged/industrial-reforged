package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.networking.data.ItemSyncData;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.CastingTableBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CastingTableBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        IItemHandler itemHandler = BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, p_60504_.getBlockEntity(p_60505_));
        if (itemHandler.getStackInSlot(0).isEmpty() && p_60506_.getItemInHand(p_60507_).is(Items.AMETHYST_SHARD)) {
            itemHandler.insertItem(0, new ItemStack(Items.STICK), false);
        } else {
            PacketDistributor.ALL.noArg().send(new ItemSyncData(p_60505_, 1, new ItemStack[]{itemHandler.getStackInSlot(0)}));
            IndustrialReforged.LOGGER.debug("Sending packet");
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return createTickerHelper(p_153214_, IRBlockEntityTypes.CASTING_TABLE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(blockPos, blockState));
    }
}
