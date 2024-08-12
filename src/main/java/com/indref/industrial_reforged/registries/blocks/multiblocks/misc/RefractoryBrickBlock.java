package com.indref.industrial_reforged.registries.blocks.multiblocks.misc;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.registries.blocks.misc.CoilBlock;
import com.indref.industrial_reforged.registries.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RefractoryBrickBlock extends Block implements WrenchableBlock, DisplayBlock {
    public static final BooleanProperty HATCH_ACTIVE = BooleanProperty.create("hatch_active");

    public RefractoryBrickBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(FireboxMultiblock.FIREBOX_PART) != FireboxMultiblock.PartIndex.HATCH || !state.getValue(HATCH_ACTIVE)) return;

        for (HorizontalDirection dir : HorizontalDirection.values()) {
            Direction direction = dir.toRegularDirection();
            BlockPos relativePos = pos.relative(direction);
            if (!level.getBlockState(relativePos).canOcclude()) {
                double d0 = (double)pos.getX() + 0.5;
                double d1 = pos.getY();
                double d2 = (double)pos.getZ() + 0.5;

                if (random.nextDouble() < 0.1) {
                    level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 2.0F, 1.0F, false);
                }

                Direction.Axis direction$axis = direction.getAxis();
                double d4 = random.nextDouble() * 0.6 - 0.3;
                double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52 : d4;
                double d6 = random.nextDouble() * 6.0 / 16.0;
                double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52 : d4;
                level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6 + 0.3, d2 + d7, 0.0, 0.0, 0.0);
                level.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6 + 0.3, d2 + d7, 0.0, 0.0, 0.0);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!state.getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.UNFORMED)) {
                for (BlockPos blockPos1 : BlockUtils.getBlocksAroundSelf3x3(pos)) {
                    if (level.getBlockState(blockPos1).getBlock() instanceof CoilBlock
                            && level.getBlockState(blockPos1).getValue(FireboxMultiblock.FIREBOX_PART).equals(FireboxMultiblock.PartIndex.COIL)) {
                        MultiblockHelper.unform(IRMultiblocks.REFRACTORY_FIREBOX.get(), blockPos1, level);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult p_60508_) {
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
        pBuilder.add(FireboxMultiblock.FIREBOX_PART, HATCH_ACTIVE);
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
