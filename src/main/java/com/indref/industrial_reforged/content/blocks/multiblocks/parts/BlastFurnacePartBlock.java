package com.indref.industrial_reforged.content.blocks.multiblocks.parts;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.part.CruciblePartBlockEntity;
import com.indref.industrial_reforged.content.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.part.BlastFurnacePartBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.part.FireboxPartBlockEntity;
import com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableEntityBlock;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.indref.industrial_reforged.content.multiblocks.CrucibleMultiblock.CRUCIBLE_WALL;

public class BlastFurnacePartBlock extends RotatableEntityBlock implements DisplayBlock {
    public BlastFurnacePartBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlastFurnacePartBlockEntity be = BlockUtils.getBE(level, pos, BlastFurnacePartBlockEntity.class);
        if (level.getBlockEntity(be.getControllerPos()) instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity) {
            Utils.openMenu(player, blastFurnaceBlockEntity);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public void displayOverlay(List<Component> displayText, Player player, Level level, ItemStack itemStack, BlockPos scannedBlockPos, BlockState scannedBlock) {
        BlockEntity blockEntity = level.getBlockEntity(scannedBlockPos);
        if (blockEntity instanceof FireboxPartBlockEntity partBlockEntity) {
            BlockPos controllerPos = partBlockEntity.getControllerPos();
            BlockEntity controllerBE = level.getBlockEntity(controllerPos);
            if (controllerBE instanceof FireboxBlockEntity) {
                if (itemStack.is(IRItems.SCANNER.get())) {
                    DisplayUtils.displayFluidInfo(displayText, scannedBlock, scannedBlockPos, level);
                } else if (itemStack.is(IRItems.THERMOMETER.get())) {
                    DisplayUtils.displayHeatInfo(displayText, scannedBlock, scannedBlockPos, level);
                }
            }
        }
    }

    @Override
    protected void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if (!p_60515_.is(p_60518_.getBlock())) {
            BlockEntity blockEntity = p_60516_.getBlockEntity(p_60517_);
            if (blockEntity instanceof BlastFurnacePartBlockEntity partBlockEntity) {
                if (p_60516_.getBlockEntity(partBlockEntity.getControllerPos()) instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity) {
                    MultiblockHelper.unform(IRMultiblocks.BLAST_FURNACE.get(), blastFurnaceBlockEntity.getActualBlockEntityPos(), p_60516_);
                }
            }
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(BlastFurnaceMultiblock.BRICK_STATE));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return IRBlocks.BLAST_FURNACE_BRICKS.toStack();
    }

    @Override
    public List<ItemLike> getCompatibleItems() {
        return List.of(IRItems.SCANNER.get(), IRItems.THERMOMETER.get());
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BlastFurnacePartBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BlastFurnacePartBlockEntity(p_153215_, p_153216_);
    }
}
