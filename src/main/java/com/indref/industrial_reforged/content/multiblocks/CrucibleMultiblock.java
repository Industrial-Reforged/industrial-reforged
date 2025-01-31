package com.indref.industrial_reforged.content.multiblocks;

import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.blocks.multiblocks.parts.CruciblePartBlock;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockDefinition;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CrucibleMultiblock(CrucibleTier tier) implements Multiblock {
    public static final EnumProperty<WallStates> CRUCIBLE_WALL = EnumProperty.create("crucible_wall", WallStates.class);

    @Override
    public Block getUnformedController() {
        return tier.getUnformedController();
    }

    @Override
    public Block getFormedController() {
        return tier.getFormedController();
    }

    @Override
    public MultiblockLayer[] getLayout() {
        return new MultiblockLayer[]{
                layer(
                        1, 0, 0, 0, 1,
                        3, 0, 2, 0, 3,
                        1, 0, 0, 0, 1
                ),
                layer(
                        1, 0, 0, 0, 1,
                        3, 0, 1, 0, 3,
                        1, 0, 0, 0, 1
                ),
                layer(
                        1, 0, 0, 0, 1,
                        3, 0, 1, 0, 3,
                        1, 0, 0, 0, 1
                )
        };
    }

    @Override
    public MultiblockDefinition getDefinition() {
        MultiblockDefinition def = new MultiblockDefinition();
        def.put(0, tier.getUnformedPart());
        def.put(1, null);
        def.put(2, getUnformedController());
        def.put(3, IRBlocks.IRON_FENCE.get());
        return def;
    }

    @Override
    public @Nullable BlockEntityType<? extends MultiblockEntity> getMultiBlockEntityType() {
        return IRBlockEntityTypes.CRUCIBLE.get();
    }

    @Override
    public @Nullable BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockData multiblockData, @Nullable Player player) {
        BlockState currentBlock = level.getBlockState(blockPos);
        Direction mDirection = multiblockData.direction().toRegularDirection();
        Direction pDirection = player != null ? player.getDirection() : mDirection;
        Direction direction = mDirection.getAxis() == pDirection.getAxis() ? pDirection : mDirection;
        if (currentBlock.is(tier.getUnformedPart())
                || currentBlock.is(tier.getFormedPart())
                || currentBlock.is(IRBlocks.IRON_FENCE)) {
            return tier.getFormedPart()
                    .defaultBlockState()
                    .setValue(CRUCIBLE_WALL, switch (layerIndex) {
                        case 1, 3, 11, 13 -> layoutIndex == 0 ? WallStates.EDGE_BOTTOM : WallStates.EDGE_TOP;
                        case 2, 6, 8, 12 -> layoutIndex == 0 ? WallStates.WALL_BOTTOM : WallStates.WALL_TOP;
                        case 5, 9 -> WallStates.FENCE;
                        default -> WallStates.WALL_TOP;
                    })
                    .setValue(CruciblePartBlock.FACING, switch (direction) {
                        case NORTH, SOUTH -> switch (layerIndex) {
                            case 2, 3 -> Direction.WEST;
                            case 8, 13 -> Direction.NORTH;
                            case 11, 12 -> Direction.EAST;
                            default -> Direction.SOUTH;
                        };
                        default -> switch (layerIndex) {
                            case 2, 3 -> Direction.NORTH;
                            case 8, 13 -> Direction.EAST;
                            case 11, 12 -> Direction.SOUTH;
                            default -> Direction.WEST;
                        };
                    });
        } else if (currentBlock.is(tier.getUnformedController()) || currentBlock.is(tier.getFormedController())) {
            return tier.getFormedController().defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        }
        return null;
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);

        return blockState.hasProperty(CRUCIBLE_WALL) || blockState.is(getFormedController());
    }

    @Override
    public MultiblockLayer layer(int... layer) {
        return Multiblock.super.layer(layer).setWidths(5, 3);
    }

    public enum WallStates implements StringRepresentable {
        EDGE_BOTTOM("edge_bottom"),
        EDGE_TOP("edge_top"),
        WALL_BOTTOM("wall_bottom"),
        WALL_TOP("wall_top"),
        FENCE("fence");

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
