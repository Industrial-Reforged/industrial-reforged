package com.indref.industrial_reforged.registries.blocks.multiblocks.controller;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlock;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FireboxControllerBlock extends ContainerBlock implements DisplayBlock {
    private final FireboxTier tier;

    public FireboxControllerBlock(Properties properties, FireboxTier tier) {
        super(properties);
        this.tier = tier;
    }

    public FireboxTier getTier() {
        return tier;
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
        return simpleCodec(props -> new FireboxControllerBlock(props, tier));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        MultiblockHelper.unform(IRMultiblocks.REFRACTORY_FIREBOX.get(), blockPos, level);

        super.onRemove(blockState, level, blockPos, newState, p_60519_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FireboxMultiblock.FIREBOX_PART));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return IRBlocks.COIL.toStack();
    }

    @Override
    public void displayOverlay(List<Component> components, BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        DisplayUtils.displayHeatInfo(components, scannedBlock, scannedBlockPos, level);
    }

    @Override
    public List<DisplayItem> getCompatibleItems() {
        return List.of(IRItems.THERMOMETER.get());
    }
}
