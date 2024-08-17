package com.indref.industrial_reforged.util.capabilities;

import com.indref.industrial_reforged.api.capabilities.IOActions;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Direction;

import java.util.Map;

public final class SidedCapUtils {
    public static Map<Direction, Pair<IOActions, int[]>> allInsert(int ...slots) {
        return Map.of(
                Direction.NORTH, Pair.of(IOActions.INSERT, slots),
                Direction.EAST, Pair.of(IOActions.INSERT, slots),
                Direction.SOUTH, Pair.of(IOActions.INSERT, slots),
                Direction.WEST, Pair.of(IOActions.INSERT, slots),
                Direction.UP, Pair.of(IOActions.INSERT, slots),
                Direction.DOWN, Pair.of(IOActions.INSERT, slots)
        );
    }

    public static Map<Direction, Pair<IOActions, int[]>> allExtract(int ...slots) {
        return Map.of(
                Direction.NORTH, Pair.of(IOActions.EXTRACT, slots),
                Direction.EAST, Pair.of(IOActions.EXTRACT, slots),
                Direction.SOUTH, Pair.of(IOActions.EXTRACT, slots),
                Direction.WEST, Pair.of(IOActions.EXTRACT, slots),
                Direction.UP, Pair.of(IOActions.EXTRACT, slots),
                Direction.DOWN, Pair.of(IOActions.EXTRACT, slots)
        );
    }
}
