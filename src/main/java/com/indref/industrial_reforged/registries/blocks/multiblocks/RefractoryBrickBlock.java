package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class RefractoryBrickBlock extends Block implements WrenchableBlock, DisplayBlock {
    public RefractoryBrickBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState ignored, boolean p_60519_) {
        if (!blockState.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED)) {
            for (BlockPos blockPos1 : BlockUtils.getBlocksAroundSelf3x3(blockPos)) {
                if (level.getBlockState(blockPos1).getBlock() instanceof CoilBlock
                        && level.getBlockState(blockPos1).getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.COIL)) {
                    MultiblockHelper.unform(IRMultiblocks.REFRACTORY_FIREBOX.get(), blockPos1, level);
                    break;
                }
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult p_60508_) {
        if (!level.isClientSide() && !blockState.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED)) {
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
        pBuilder.add(FireboxMultiblock.FIREBOX_PART);
    }

    @Override
    public void displayOverlay(List<Component> displayText, BlockState scannedBlock, BlockPos blockPos, Level level) {
        if (scannedBlock.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED))
            return;

        for (BlockPos pos : BlockUtils.getBlocksAroundSelf3x3(blockPos)) {
            BlockEntity fireBoxBlockEntity = level.getBlockEntity(pos);
            if (fireBoxBlockEntity instanceof FireboxBlockEntity) {
                displayText.addAll(DisplayUtils.displayHeatInfo(fireBoxBlockEntity, scannedBlock, Component.literal("Firebox")));
            }
        }
    }

    @Override
    public List<DisplayItem> getCompatibleItems() {
        return List.of((DisplayItem) IRItems.THERMOMETER.get());
    }
}
