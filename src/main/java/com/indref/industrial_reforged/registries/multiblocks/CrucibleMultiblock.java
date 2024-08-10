package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record CrucibleMultiblock(CrucibleTier tier) implements Multiblock {
    public static final EnumProperty<WallStates> CRUCIBLE_WALL = EnumProperty.create("crucible_wall", CrucibleMultiblock.WallStates.class);

    @Override
    public Block getController() {
        return tier.getController();
    }

    @Override
    public int[][] getLayout() {
        return new int[][]{
                {
                        0, 0, 0,
                        0, 2, 0,
                        0, 0, 0
                },
                {
                        0, 0, 0,
                        0, 1, 0,
                        0, 0, 0
                }
        };
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
    public Optional<MultiblockDirection> getFixedDirection() {
        return Optional.of(MultiblockDirection.NORTH);
    }

    @Override
    public Optional<BlockState> formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY) {
        return Optional.empty();
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos, BlockPos controllerPos) {
        BlockState blockState = level.getBlockState(blockPos);

        return !blockState.hasProperty(CRUCIBLE_WALL);
    }

    public enum WallStates implements StringRepresentable {
        TOP("top"),
        MIDDLE("middle"),
        BOTTOM("bottom");

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
