package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoilBlock extends BaseEntityBlock implements Wrenchable, DisplayBlock {
    public CoilBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CoilBlock::new);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        super.onRemove(blockState, level, blockPos, newState, p_60519_);

        if (!blockState.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED)) {
            MultiblockHelper.unform(IRMultiblocks.REFRACTORY_FIREBOX.get(), blockPos, level);
        }

        if (level.getBlockEntity(blockPos) instanceof FireboxBlockEntity fireboxBlockEntity && newState.is(Blocks.AIR)) {
            fireboxBlockEntity.drops();
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FireboxMultiblock.FIREBOX_PART);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;

        return createTickerHelper(blockEntityType, IRBlockEntityTypes.FIREBOX.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FireboxBlockEntity(blockPos, blockState);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand p_316595_, BlockHitResult p_316140_) {
        BlockEntity fireBoxBlockEntity = level.getBlockEntity(blockPos);
        if (!level.isClientSide() && !blockState.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED)) {
            player.openMenu((FireboxBlockEntity) fireBoxBlockEntity, blockPos);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    public List<Component> displayOverlay(BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        if (scannedBlock.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED))
            return List.of();

        return DisplayUtils.displayHeatInfo(level.getBlockEntity(scannedBlockPos), scannedBlock, Component.translatable("Firebox"));
    }

    @Override
    public List<DisplayItem> getCompatibleItems() {
        return List.of((DisplayItem) IRItems.THERMOMETER.get());
    }
}
