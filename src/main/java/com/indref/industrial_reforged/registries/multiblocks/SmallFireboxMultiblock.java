package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.blocks.RotatableEntityBlock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockLayer;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.util.MultiblockHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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

import java.util.Optional;

public record SmallFireboxMultiblock(FireboxTier tier) implements IFireboxMultiblock {
    public static final EnumProperty<SmallFireboxMultiblock.FireboxState> FIREBOX_STATE = EnumProperty.create("firebox_state",
            SmallFireboxMultiblock.FireboxState.class);

    @Override
    public Block getController() {
        return IRBlocks.SMALL_FIREBOX_HATCH.get();
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
    public Optional<BlockPos> getControllerPos(BlockPos multiblockPos, Level level) {
        return Optional.empty();
    }

    @Override
    public Int2ObjectMap<@Nullable Block> getDefinition() {
        Int2ObjectMap<Block> def = new Int2ObjectOpenHashMap<>();
        def.put(0, IRBlocks.SMALL_FIREBOX_HATCH.get());
        return def;
    }

    @Override
    public @NotNull BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockHelper.UnformedMultiblock unformedMultiblock, @Nullable Player player) {
        BlockState blockState = level.getBlockState(blockPos);
        if (layoutIndex == 0) {
            return blockState.setValue(RotatableEntityBlock.FACING, getCorrectDirection(layerIndex, unformedMultiblock.direction())).setValue(FIREBOX_STATE, FireboxState.FORMED);
        }
        return blockState;
    }

    @SuppressWarnings("DataFlowIssue")
    private static @NotNull Direction getCorrectDirection(int index, HorizontalDirection direction) {
        return switch (direction) {
            case NORTH -> switch (index) {
                case 0 -> Direction.SOUTH;
                case 1 -> Direction.WEST;
                case 2 -> Direction.EAST;
                case 3 -> Direction.NORTH;
                default -> null;
            };
            case EAST -> switch (index) {
                case 0 -> Direction.WEST;
                case 1 -> Direction.NORTH;
                case 2 -> Direction.SOUTH;
                case 3 -> Direction.EAST;
                default -> null;
            };
            case SOUTH -> switch (index) {
                case 0 -> Direction.NORTH;
                case 1 -> Direction.EAST;
                case 2 -> Direction.WEST;
                case 3 -> Direction.SOUTH;
                default -> null;
            };
            case WEST -> switch (index) {
                case 0 -> Direction.EAST;
                case 1 -> Direction.SOUTH;
                case 2 -> Direction.NORTH;
                case 3 -> Direction.WEST;
                default -> null;
            };
        };
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos) {
        BlockState state = level.getBlockState(blockPos);
        return state.hasProperty(FIREBOX_STATE) && !state.getValue(FIREBOX_STATE).equals(FireboxState.UNFORMED);
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
