package com.indref.industrial_reforged.api.multiblocks;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public enum MultiblockDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Direction toMCDir() {
        return switch (this) {
            case NORTH -> Direction.NORTH;
            case EAST -> Direction.EAST;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
        };
    }

    public static @Nullable MultiblockDirection fromMCDir(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> null;
        };
    }
}