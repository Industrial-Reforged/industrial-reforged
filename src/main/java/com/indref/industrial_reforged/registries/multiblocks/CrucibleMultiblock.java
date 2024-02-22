package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CrucibleWallBlockEntity;
import com.indref.industrial_reforged.registries.blocks.multiblocks.CrucibleWallBlock;
import com.indref.industrial_reforged.util.MultiblockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CrucibleMultiblock(CrucibleTier tier) implements Multiblock {

    @Override
    public Block getController() {
        return tier.getController();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return List.of(
                List.of(
                        0, 0, 0,
                        0, 2, 0,
                        0, 0, 0
                ),
                List.of(
                        0, 0, 0,
                        0, 1, 0,
                        0, 0, 0
                )
        );
    }

    @Override
    public Map<Integer, @Nullable Block> getDefinition() {
        Map<Integer, Block> def = new HashMap<>();
        def.put(0, tier.getCrucibleWallBlock());
        def.put(1, null);
        def.put(2, tier.getController());
        return def;
    }

    @Override
    public MultiblockDirection getFixedDirection() {
        return MultiblockDirection.NORTH;
    }

    @Override
    public void formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY) {
        BlockState currentBlock = level.getBlockState(blockPos);
        if (currentBlock.is(tier.getCrucibleWallBlock())) {
            MultiblockUtils.setAndUpdate(level, blockPos, currentBlock, IRBlocks.CERAMIC_CRUCIBLE_WALL.get()
                    .defaultBlockState()
                    .setValue(CrucibleWallBlock.CRUCIBLE_WALL, switch (index) {
                        case 0, 2, 6, 8 -> switch (indexY) {
                            case 0 -> WallStates.EDGE_BOTTOM;
                            default -> WallStates.EDGE_TOP;
                        };
                        case 1, 3, 5, 7 -> switch (indexY) {
                            case 0 -> WallStates.WALL_BOTTOM;
                            default -> WallStates.WALL_TOP;
                        };
                        default -> WallStates.WALL_TOP;
                    })
                    .setValue(CrucibleWallBlock.FACING, switch (index) {
                        case 1, 2 -> Direction.EAST;
                        case 5, 8 -> Direction.SOUTH;
                        case 6, 7 -> Direction.WEST;
                        default -> Direction.NORTH;
                    }));
            CrucibleWallBlockEntity blockEntity = (CrucibleWallBlockEntity) level.getBlockEntity(blockPos);
            blockEntity.controllerPos = controllerPos;
        } else if (currentBlock.is(IRBlocks.TERRACOTTA_BRICK_SLAB.get())) {
            MultiblockUtils.setAndUpdate(level, blockPos, currentBlock, IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get().defaultBlockState());
        }
    }

    @Override
    public void unformBlock(Level level, BlockPos blockPos, BlockPos controllerPos) {

    }


    public enum WallStates implements StringRepresentable {
        EDGE_BOTTOM("edge_bottom"),
        EDGE_TOP("edge_top"),
        WALL_BOTTOM("wall_bottom"),
        WALL_TOP("wall_top");

        private final String name;

        WallStates(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
