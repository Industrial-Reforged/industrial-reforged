package com.indref.industrial_reforged.content.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.SmallFireboxBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableEntityBlock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockDefinition;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SmallFireboxMultiblock(FireboxTier tier) implements IFireboxMultiblock {
    public static final IntegerProperty FIREBOX_PART = IntegerProperty.create("firebox_part", 0, 3);

    @Override
    public Block getUnformedController() {
        return IRBlocks.SMALL_FIREBOX_HATCH.get();
    }

    @Override
    public Block getFormedController() {
        return getUnformedController();
    }

    @Override
    public MultiblockLayer[] getLayout() {
        return new MultiblockLayer[]{
                layer(
                        0, 0,
                        0, 0
                )
        };
    }

    @Override
    public @Nullable BlockPos getControllerPos(BlockPos multiblockPos, Level level) {
        BlockEntity be = level.getBlockEntity(multiblockPos);
        if (be instanceof SmallFireboxBlockEntity smallFireboxBlockEntity) {
            return smallFireboxBlockEntity.getActualBlockEntityPos();
        }
        return null;
    }

    @Override
    public MultiblockDefinition getDefinition() {
        MultiblockDefinition def = new MultiblockDefinition();
        def.put(0, IRBlocks.SMALL_FIREBOX_HATCH.get());
        return def;
    }

    @Override
    public BlockEntityType<? extends MultiblockEntity> getMultiBlockEntityType() {
        return IRBlockEntityTypes.SMALL_FIREBOX.get();
    }

    @Override
    public @NotNull BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockData multiblockData, @Nullable Player player) {
        BlockState blockState = level.getBlockState(blockPos);
        if (layoutIndex == 0) {
            return blockState
                    .setValue(FIREBOX_PART, fireboxPart(layerIndex, multiblockData.direction()))
                    .setValue(FORMED, true);
        }
        return blockState;
    }

    public int fireboxPart(int layerIndex, HorizontalDirection direction) {
        IndustrialReforged.LOGGER.debug("direction: {}", direction);
        return switch (direction) {
            case NORTH -> layerIndex;
            case EAST -> switch (layerIndex) {
                case 0 -> 1;
                case 1 -> 3;
                case 2 -> 0;
                case 3 -> 2;
                default -> -1;
            };
            case SOUTH -> 3 - layerIndex;
            case WEST -> switch (layerIndex) {
                case 0 -> 2;
                case 1 -> 0;
                case 2 -> 3;
                case 3 -> 1;
                default -> -1;
            };
        };
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos) {
        BlockState state = level.getBlockState(blockPos);
        return state.hasProperty(FORMED) && state.getValue(FORMED);
    }

    @Override
    public FireboxTier getTier() {
        return tier;
    }
}
