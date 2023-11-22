package com.indref.industrial_reforged.registries.blocks.misc;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class CrucibleWallBlock extends Block {
    private static final EnumProperty<WallStates> CRUCIBLE_WALL = EnumProperty.create("crucible_wall", WallStates.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public CrucibleWallBlock(Properties properties) {
        super(properties);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer().isCrouching()){
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        }
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(CRUCIBLE_WALL, FACING);
    }

    public enum WallStates implements StringRepresentable {
        EDGE_LOWER("edge_lower"),
        EDGE_UPPER("edge_upper"),
        WALL_LOWER("wall_lower"),
        WALL_UPPER("wall_upper");

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
