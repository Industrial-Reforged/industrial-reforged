package com.indref.industrial_reforged.registries.blocks.misc;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// TODO: Seperate coil & firebox
public class CoilBlock extends ContainerBlock implements WrenchableBlock, DisplayBlock {
    public CoilBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.FIREBOX.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CoilBlock::new);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        super.onRemove(blockState, level, blockPos, newState, p_60519_);

        if (!blockState.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED)) {
            MultiblockHelper.unform(IRMultiblocks.REFRACTORY_FIREBOX.get(), blockPos, level);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FireboxMultiblock.FIREBOX_PART));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, BlockHitResult p_60508_) {
        if (!p_60503_.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED)) {
            return super.useWithoutItem(p_60503_, p_60504_, p_60505_, p_60506_, p_60508_);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void displayOverlay(List<Component> components, BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        if (scannedBlock.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED))
            return;

        components.addAll(DisplayUtils.displayHeatInfo(level.getBlockEntity(scannedBlockPos), scannedBlock, Component.translatable("title.indref.firebox")));
    }

    @Override
    public List<DisplayItem> getCompatibleItems() {
        return List.of(IRItems.THERMOMETER.get());
    }
}
