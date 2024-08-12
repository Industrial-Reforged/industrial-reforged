package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.DynamicMultiblock;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.blocks.multiblocks.misc.BlastFurnaceBricksBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

// Why is this a record, you might ask yourself...
// The answer is: So my icons for multiblock classes all look the same :>
public record BlastFurnaceMultiblock() implements DynamicMultiblock {
    public static final EnumProperty<BrickStates> BRICK_STATE = EnumProperty.create("brick_state", BlastFurnaceMultiblock.BrickStates.class);

    @Override
    public Block getController() {
        return IRBlocks.BLAST_FURNACE_HATCH.get();
    }

    @Override
    public int[][] getLayout() {
        return new int[][]{
                {
                        1, 1,
                        1, 1
                },
                dynamicLayer(5,
                        0, 0,
                        0, 0
                )
        };
    }

    @Override
    public Map<Integer, @Nullable Block> getDefinition() {
        return Map.of(0, IRBlocks.BLAST_FURNACE_BRICKS.get(), 1, IRBlocks.BLAST_FURNACE_HATCH.get());
    }

    @Override
    public Optional<BlockState> formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY, Player player) {
        BlockState blockState = level.getBlockState(blockPos);
        if (indexY == 3) {
            return Optional.of(blockState.setValue(BRICK_STATE, BrickStates.TOP).setValue(BlastFurnaceBricksBlock.FACING, getCorrectDirection(index, direction)));
        } else {
            return Optional.of(blockState.setValue(BRICK_STATE, BrickStates.FORMED));
        }
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos, BlockPos controllerPos) {
        BlockState blockState = level.getBlockState(blockPos);

        if (!getDefinition().containsValue(blockState.getBlock())) return false;

        return !blockState.getValue(BRICK_STATE).equals(BrickStates.UNFORMED);
    }

    private static Direction getCorrectDirection(int index, MultiblockDirection direction) {
        return switch (direction) {
            case NORTH -> switch (index) {
                case 0 -> Direction.NORTH;
                case 1 -> Direction.EAST;
                case 2 -> Direction.WEST;
                case 3 -> Direction.SOUTH;
                default -> null;
            };
            case EAST -> switch (index) {
                case 0 -> Direction.EAST;
                case 1 -> Direction.SOUTH;
                case 2 -> Direction.NORTH;
                case 3 -> Direction.WEST;
                default -> null;
            };
            case SOUTH -> switch (index) {
                case 0 -> Direction.SOUTH;
                case 1 -> Direction.WEST;
                case 2 -> Direction.EAST;
                case 3 -> Direction.NORTH;
                default -> null;
            };
            case WEST -> switch (index) {
                case 0 -> Direction.WEST;
                case 1 -> Direction.NORTH;
                case 2 -> Direction.SOUTH;
                case 3 -> Direction.EAST;
                default -> null;
            };
        };
    }

    public enum BrickStates implements StringRepresentable {
        UNFORMED("unformed"),
        FORMED("formed"),
        TOP("top");

        private final String name;

        BrickStates(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
