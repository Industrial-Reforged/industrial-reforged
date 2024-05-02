package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.blocks.multiblocks.SmallFireboxHatchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record SmallFireboxMultiblock(FireboxTier tier) implements IFireboxMultiblock {
    public static final EnumProperty<SmallFireboxMultiblock.FireboxState> FIREBOX_STATE = EnumProperty.create("firebox_state",
            SmallFireboxMultiblock.FireboxState.class);
    @Override
    public Block getController() {
        return IRBlocks.SMALL_FIREBOX_HATCH.get();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return List.of(
                List.of(
                        0, 0,
                        0, 0
                ));
    }

    @Override
    public Optional<BlockPos> getControllerPos(BlockPos multiblockPos, Level level) {
        return Optional.empty();
    }

    @Override
    public Map<Integer, @Nullable Block> getDefinition() {
        return Map.of(0, IRBlocks.SMALL_FIREBOX_HATCH.get());
    }

    @Override
    public Optional<BlockState> formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY) {
        BlockState blockState = level.getBlockState(blockPos);
        if (indexY == 0) {
            return Optional.of(blockState.setValue(SmallFireboxHatchBlock.FACING, getCorrectDirection(index, direction)).setValue(FIREBOX_STATE, FireboxState.FORMED));
        }
        return Optional.of(blockState);
    }

    private static Direction getCorrectDirection(int index, MultiblockDirection direction) {
        return switch (direction) {
            case WEST -> switch (index) {
                case 0 -> Direction.EAST;
                case 1 -> Direction.SOUTH;
                case 2 -> Direction.NORTH;
                case 3 -> Direction.WEST;
                default -> null;
            };
            case NORTH -> switch (index) {
                case 0 -> Direction.SOUTH;
                case 1 -> Direction.WEST;
                case 2 -> Direction.EAST;
                case 3 -> Direction.NORTH;
                default -> null;
            };
            case SOUTH -> switch (index) {
                case 0 -> Direction.NORTH;
                case 1 -> Direction.EAST;
                case 2 -> Direction.WEST;
                case 3 -> Direction.SOUTH;
                default -> null;
            };
            case EAST -> switch (index) {
                case 0 -> Direction.WEST;
                case 1 -> Direction.NORTH;
                case 2 -> Direction.SOUTH;
                case 3 -> Direction.EAST;
                default -> null;
            };
        };
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos, BlockPos controllerPos) {
        return !level.getBlockState(blockPos).getValue(FIREBOX_STATE).equals(FireboxState.UNFORMED);
    }

    @Override
    public FireboxTier getTier() {
        return tier;
    }

    public enum FireboxState implements StringRepresentable {
        FORMED("formed"),
        UNFORMED("unformed");

        private final String name;

        FireboxState(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
