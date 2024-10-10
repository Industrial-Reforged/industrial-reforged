package com.indref.industrial_reforged.content.multiblocks;

import com.indref.industrial_reforged.api.blockentities.multiblock.MultiblockEntity;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockData;
import com.indref.industrial_reforged.api.multiblocks.MultiblockLayer;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRBlocks;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.apache.commons.lang3.IntegerRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Why is this a record, you might ask yourself...
// The answer is: So my icons for multiblock classes all look the same :>
public record BlastFurnaceMultiblock() implements Multiblock {
    public static final EnumProperty<BrickStates> BRICK_STATE = EnumProperty.create("brick_state", BlastFurnaceMultiblock.BrickStates.class);

    @Override
    public Block getUnformedController() {
        return IRBlocks.BLAST_FURNACE_HATCH.get();
    }

    @Override
    public Block getFormedController() {
        return getUnformedController();
    }

    private Block getUnformedPart() {
        return IRBlocks.BLAST_FURNACE_BRICKS.get();
    }

    private Block getFormedPart() {
        return IRBlocks.BLAST_FURNACE_PART.get();
    }

    // Note: This method is heavily documented as it serves as an
    // example on how to create multiblock layouts
    @Override
    public MultiblockLayer[] getLayout() {
        // Create a new array of multiblock layers
        // each layer is created by calling the `layer(..)`
        // method. This returns a new multiblock layer.
        // You can call methods like setDynamic(IntegerRange)
        // on it to make the layer dynamic.
        // Note that the first member of this array
        // is the lowest layer in game.
        return new MultiblockLayer[]{
                layer(
                        1, 1,
                        1, 1
                ),
                layer(
                        0, 0,
                        0, 0
                ).setDynamic(IntegerRange.of(3, 6)),
        };
    }

    // Note: This method is heavily documented as it serves as an
    // example on how to create multiblock definitions
    @Override
    public Int2ObjectMap<Block> getDefinition() {
        // Create new map to store the integer keys and their block values
        Int2ObjectMap<Block> def = new Int2ObjectOpenHashMap<>();
        // Assign each integer to a block
        // The first argument is the integer key, the second is the block.
        def.put(0, IRBlocks.BLAST_FURNACE_BRICKS.get());
        def.put(1, IRBlocks.BLAST_FURNACE_HATCH.get());
        // return the map of definitions we created
        return def;
    }

    @Override
    public BlockEntityType<? extends MultiblockEntity> getMultiBlockEntityType() {
        return IRBlockEntityTypes.BLAST_FURNACE.get();
    }

    @Override
    public @NotNull BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockData multiblockData, @Nullable Player player) {
        if (layoutIndex == multiblockData.layers().length - 1) {
            return getFormedPart().defaultBlockState().setValue(BRICK_STATE, BrickStates.TOP).setValue(BlockStateProperties.HORIZONTAL_FACING, getCorrectDirection(layerIndex, multiblockData.direction()));
        } else if (layoutIndex == 0) {
            return getFormedController().defaultBlockState().setValue(BRICK_STATE, BrickStates.FORMED);
        }
        return getFormedPart().defaultBlockState().setValue(BRICK_STATE, BrickStates.FORMED);
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.is(getUnformedPart())) return false;

        return blockState.hasProperty(BRICK_STATE) && blockState.getValue(BRICK_STATE) != BrickStates.UNFORMED;
    }

    private static Direction getCorrectDirection(int index, HorizontalDirection direction) {
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
