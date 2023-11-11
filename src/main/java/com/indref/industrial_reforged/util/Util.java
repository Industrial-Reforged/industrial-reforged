package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.capabilities.energy.network.EnergyNetsProvider;
import com.indref.industrial_reforged.capabilities.energy.network.IEnergyNets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public final class Util {
    public static final int[] EMPTY_ARRAY = new int[0];
    public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
        Map<V, K> returnMap = new HashMap<>();
        map.forEach((key, value) -> returnMap.put(value, key));
        return returnMap;
    }

    public static Direction oppositeDirection(Direction originalDirection) {
        return switch (originalDirection) {
            case DOWN -> Direction.UP;
            case UP -> Direction.DOWN;
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case WEST -> Direction.EAST;
            case EAST -> Direction.WEST;
        };
    }

    public static IEnergyNets getEnergyNets(Level level) {
        return level.getCapability(IRCapabilities.ENERGY_NETWORKS)
                .orElseThrow(() -> new NullPointerException("Missing energy networks on level"));
    }
}
