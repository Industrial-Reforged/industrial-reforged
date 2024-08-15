package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockLayer;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.blocks.multiblocks.misc.CrucibleWallBlock;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CrucibleMultiblock(CrucibleTier tier) implements Multiblock {
    public static final EnumProperty<WallStates> CRUCIBLE_WALL = EnumProperty.create("crucible_wall", CrucibleMultiblock.WallStates.class);

    @Override
    public Block getController() {
        return tier.getController();
    }

    @Override
    public MultiblockLayer[] getLayout() {
        return new MultiblockLayer[]{
                layer(
                        0, 0, 0,
                        0, 2, 0,
                        0, 0, 0
                ),
                layer(
                        0, 0, 0,
                        0, 1, 0,
                        0, 0, 0
                )
        };
    }

    @Override
    public Int2ObjectMap<Block> getDefinition() {
        Int2ObjectMap<Block> def = new Int2ObjectOpenHashMap<>();
        def.put(0, tier.getCrucibleWallBlock());
        def.put(1, null);
        def.put(2, tier.getController());
        return def;
    }

    @Override
    public @Nullable BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockHelper.UnformedMultiblock unformedMultiblock, @Nullable Player player) {
        BlockState currentBlock = level.getBlockState(blockPos);
        if (currentBlock.is(tier.getCrucibleWallBlock()) || currentBlock.is(IRBlocks.CERAMIC_CRUCIBLE_WALL.get())) {
            return IRBlocks.CERAMIC_CRUCIBLE_WALL.get()
                    .defaultBlockState()
                    .setValue(CRUCIBLE_WALL, switch (layerIndex) {
                        case 0, 2, 6, 8 -> layoutIndex == 0 ? WallStates.EDGE_BOTTOM : WallStates.EDGE_TOP;
                        case 1, 3, 5, 7 -> layoutIndex == 0 ? WallStates.WALL_BOTTOM : WallStates.WALL_TOP;
                        default -> WallStates.WALL_TOP;
                    })
                    .setValue(CrucibleWallBlock.FACING, switch (layerIndex) {
                        case 1, 2 -> Direction.EAST;
                        case 5, 8 -> Direction.SOUTH;
                        case 6, 7 -> Direction.WEST;
                        default -> Direction.NORTH;
                    }
            );
        } else if (currentBlock.is(IRBlocks.TERRACOTTA_BRICK_SLAB.get())) {
            return IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get().defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, player != null ? player.getDirection(): unformedMultiblock.direction().toRegularDirection());
        }
        return null;
    }

    @Override
    public @NotNull HorizontalDirection getFixedDirection() {
        return HorizontalDirection.NORTH;
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);

        return blockState.hasProperty(CRUCIBLE_WALL);
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
