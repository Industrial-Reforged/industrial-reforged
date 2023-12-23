package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.capabilities.EnetsSavedData;
import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.capabilities.energy.network.EnergyNets;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.fluids.FluidStack;

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

    public static EnetsSavedData getEnergyNets(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(() -> new EnetsSavedData(level),
                        (CompoundTag nbt) -> EnetsSavedData.load(nbt, level)),
                "enets");
    }

    public static String fluidStackToString(FluidStack fluidStack) {
        return "FluidStack { fluid: " + fluidToString(fluidStack.getFluid()) + ", amount: " + fluidStack.getAmount() + " }";
    }

    public static String fluidToString(Fluid fluid) {
        return "Fluid { " + "type: " + fluid.getFluidType() + " }";
    }
}
