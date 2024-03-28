package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.FireBoxMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class RefractoryBrickBlock extends Block implements Wrenchable, DisplayBlock {
    public RefractoryBrickBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState ignored, boolean p_60519_) {
        if (!blockState.getValue(FireBoxMultiblock.FIREBOX_PART).equals(FireBoxMultiblock.PartIndex.UNFORMED)) {
            for (BlockPos blockPos1 : BlockUtils.getBlocksAroundSelf3x3(blockPos)) {
                if (level.getBlockState(blockPos1).getBlock() instanceof CoilBlock
                        && level.getBlockState(blockPos1).getValue(FireBoxMultiblock.FIREBOX_PART).equals(FireBoxMultiblock.PartIndex.COIL)) {
                    MultiblockUtils.unform(IRMultiblocks.REFRACTORY_FIREBOX.get(), blockPos1, level);
                    break;
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide() && !blockState.getValue(FireBoxMultiblock.FIREBOX_PART).equals(FireBoxMultiblock.PartIndex.UNFORMED)) {
            for (BlockPos pos : BlockUtils.getBlocksAroundSelf3x3(blockPos)) {
                BlockEntity fireBoxBlockEntity = level.getBlockEntity(pos);
                if (fireBoxBlockEntity instanceof FireboxBlockEntity) {
                    player.openMenu((FireboxBlockEntity) fireBoxBlockEntity, pos);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FireBoxMultiblock.FIREBOX_PART);
    }

    @Override
    public List<Component> displayOverlay(BlockState scannedBlock, BlockPos blockPos, Level level) {
        if (scannedBlock.getValue(FireBoxMultiblock.FIREBOX_PART).equals(FireBoxMultiblock.PartIndex.UNFORMED))
            return List.of();

        for (BlockPos pos : BlockUtils.getBlocksAroundSelf3x3(blockPos)) {
            BlockEntity fireBoxBlockEntity = level.getBlockEntity(pos);
            if (fireBoxBlockEntity instanceof FireboxBlockEntity) {
                return DisplayUtils.displayHeatInfo(fireBoxBlockEntity, scannedBlock, Component.literal("Firebox"));
            }
        }
        return List.of();
    }

    @Override
    public List<DisplayItem> getCompatibleItems() {
        return List.of((DisplayItem) IRItems.THERMOMETER.get());
    }
}
