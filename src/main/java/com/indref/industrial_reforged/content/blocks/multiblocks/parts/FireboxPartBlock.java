package com.indref.industrial_reforged.content.blocks.multiblocks.parts;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.items.tools.DisplayItem;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.part.FireboxPartBlockEntity;
import com.indref.industrial_reforged.content.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class FireboxPartBlock extends BaseEntityBlock implements DisplayBlock {
    public static final BooleanProperty HATCH_ACTIVE = BooleanProperty.create("hatch_active");

    public FireboxPartBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FireboxMultiblock.FIREBOX_PART, HATCH_ACTIVE);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(FireboxPartBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new FireboxPartBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(FireboxMultiblock.FIREBOX_PART) != FireboxMultiblock.PartIndex.HATCH || !state.getValue(HATCH_ACTIVE))
            return;

        for (HorizontalDirection dir : HorizontalDirection.values()) {
            Direction direction = dir.toRegularDirection();
            BlockPos relativePos = pos.relative(direction);
            if (!level.getBlockState(relativePos).canOcclude()) {
                double d0 = (double) pos.getX() + 0.5;
                double d1 = pos.getY();
                double d2 = (double) pos.getZ() + 0.5;

                if (random.nextDouble() < 0.1) {
                    level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 2.0F, 1.0F, false);
                }

                Direction.Axis direction$axis = direction.getAxis();
                double d4 = random.nextDouble() * 0.6 - 0.3;
                double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52 : d4;
                double d6 = random.nextDouble() * 6.0 / 16.0;
                double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52 : d4;
                level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6 + 0.3, d2 + d7, 0.0, 0.0, 0.0);
                level.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6 + 0.3, d2 + d7, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity thisBlockEntity = level.getBlockEntity(pos);
            if (thisBlockEntity instanceof FireboxPartBlockEntity partBE) {
                BlockPos controllerPos = partBE.getControllerPos();
                boolean success = IRMultiblocks.CRUCIBLE_CERAMIC.get().unform(controllerPos, level);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult p_60508_) {
        if (level.getBlockEntity(blockPos) instanceof FireboxPartBlockEntity fireboxPartBlockEntity) {
            BlockEntity blockEntity = level.getBlockEntity(fireboxPartBlockEntity.getControllerPos());
            if (blockEntity instanceof FireboxBlockEntity fireboxBlockEntity) {
                player.openMenu(fireboxBlockEntity, fireboxBlockEntity.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return IRBlocks.REFRACTORY_BRICK.toStack();
    }

    @Override
    public void displayOverlay(List<Component> displayText, Player player, Level level, ItemStack itemStack, BlockPos blockPos, BlockState scannedBlock) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if (blockEntity instanceof FireboxPartBlockEntity fireboxPartBlockEntity) {
            BlockPos controllerPos = fireboxPartBlockEntity.getControllerPos();
            BlockEntity fireboxBlockEntity = level.getBlockEntity(controllerPos);
            if (fireboxBlockEntity instanceof FireboxBlockEntity) {
                DisplayUtils.displayHeatInfo(displayText, fireboxBlockEntity.getBlockState(), fireboxBlockEntity.getBlockPos(), level);
            }
        }
    }

    @Override
    public List<ItemLike> getCompatibleItems() {
        return Collections.singletonList(IRItems.THERMOMETER.get());
    }
}
