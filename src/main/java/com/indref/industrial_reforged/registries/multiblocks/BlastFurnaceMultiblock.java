package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.registries.blocks.multiblocks.BlastFurnaceBricksBlock;
import com.indref.industrial_reforged.util.BlockUtils;
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

public record BlastFurnaceMultiblock() implements Multiblock {
    public static final EnumProperty<BrickStates> BRICK_STATE = EnumProperty.create("brick_state", BlastFurnaceMultiblock.BrickStates.class);

    @Override
    public Block getController() {
        return IRBlocks.BLAST_FURNACE_HATCH.get();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return List.of(
                List.of(
                        1, 1,
                        1, 1
                ),
                List.of(
                        0, 0,
                        0, 0
                ),
                List.of(
                        0, 0,
                        0, 0
                ),
                List.of(
                        0, 0,
                        0, 0
                )
        );
    }

    @Override
    public Map<Integer, @Nullable Block> getDefinition() {
        return Map.of(0, IRBlocks.BLAST_FURNACE_BRICKS.get(), 1, IRBlocks.BLAST_FURNACE_HATCH.get());
    }

    @Override
    public Optional<BlockState> formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY) {
        BlockState blockState = level.getBlockState(blockPos);
        if (indexY == 3) {
            return Optional.of(blockState.setValue(BRICK_STATE, BrickStates.TOP).setValue(BlastFurnaceBricksBlock.FACING, getCorrectDirection(index, direction)));
        } else {
            return Optional.of(blockState.setValue(BRICK_STATE, BrickStates.FORMED));
        }
    }

    @Override
    public void afterFormBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int indexX, int indexY) {
        // Check if the formed block is a hatch
        if (level.getBlockEntity(blockPos) instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity) {
            for (BlockPos blockPos1 : BlockUtils.getBlocksAroundSelf3x3(blockPos)) {
                if (level.getBlockEntity(blockPos1) instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity1) {
                    Optional<BlockPos> mainControllerPos = blastFurnaceBlockEntity1.getMainControllerPos();
                    if (mainControllerPos.isPresent()) {
                        blastFurnaceBlockEntity.setMainControllerPos(mainControllerPos.get());
                        blastFurnaceBlockEntity.setMainController(false);
                        return;
                    }
                }
            }

            blastFurnaceBlockEntity.setMainControllerPos(blockPos);
            blastFurnaceBlockEntity.setMainController(true);
        }
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos, BlockPos controllerPos) {
        // TODO: Add actual check
        return true;
    }

    private static Direction getCorrectDirection(int index, MultiblockDirection direction) {
        return switch (direction) {
            case WEST -> switch (index) {
                case 0 -> Direction.WEST;
                case 1 -> Direction.NORTH;
                case 2 -> Direction.SOUTH;
                case 3 -> Direction.EAST;
                default -> null;
            };
            case NORTH -> switch (index) {
                case 0 -> Direction.NORTH;
                case 1 -> Direction.EAST;
                case 2 -> Direction.WEST;
                case 3 -> Direction.SOUTH;
                default -> null;
            };
            case SOUTH -> switch (index) {
                case 0 -> Direction.SOUTH;
                case 1 -> Direction.WEST;
                case 2 -> Direction.EAST;
                case 3 -> Direction.NORTH;
                default -> null;
            };
            case EAST -> switch (index) {
                case 0 -> Direction.EAST;
                case 1 -> Direction.SOUTH;
                case 2 -> Direction.NORTH;
                case 3 -> Direction.WEST;
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
