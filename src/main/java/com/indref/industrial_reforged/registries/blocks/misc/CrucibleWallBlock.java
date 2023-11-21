package com.indref.industrial_reforged.registries.blocks.misc;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class CrucibleWallBlock extends Block {
    private static final EnumProperty<WallStates> CRUCIBLE_WALL = EnumProperty.create("crucibleWall", WallStates.class);
    public CrucibleWallBlock(Properties properties) {
        super(properties);
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
