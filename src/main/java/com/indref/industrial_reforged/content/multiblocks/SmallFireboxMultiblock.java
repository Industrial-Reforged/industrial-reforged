package com.indref.industrial_reforged.content.multiblocks;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SmallFireboxMultiblock(FireboxTier tier) implements IFireboxMultiblock {
    public static final EnumProperty<SmallFireboxMultiblock.FireboxState> FIREBOX_STATE = EnumProperty.create("firebox_state",
            SmallFireboxMultiblock.FireboxState.class);

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
            return blockState.setValue(RotatableEntityBlock.FACING, getCorrectDirection(layerIndex, multiblockData.direction())).setValue(FIREBOX_STATE, FireboxState.FORMED);
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
