package com.indref.industrial_reforged.util;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Utils {
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

    public static String fluidStackToString(FluidStack fluidStack) {
        return "FluidStack { fluid: " + fluidToString(fluidStack.getFluid()) + ", amount: " + fluidStack.getAmount() + " }";
    }

    public static <T extends BlockEntity & MenuProvider> void openMenu(Player player, T blockEntity) {
        if (blockEntity != null) {
            player.openMenu(blockEntity, blockEntity.getBlockPos());
        }
    }

    public static String fluidToString(Fluid fluid) {
        return "Fluid { " + "type: " + fluid.getFluidType() + " }";
    }

    public static int[] splitNumberEvenly(int number, int parts) {
        if (parts <= 0) {
            throw new IllegalArgumentException("Number of parts must be greater than 0");
        }

        int[] result = new int[parts];
        int remainder = number % parts;
        int quotient = number / parts;

        // Distribute the remainder evenly among the first 'remainder' parts
        for (int i = 0; i < remainder; i++) {
            result[i] = quotient + 1;
        }

        // Distribute the remaining parts
        for (int i = remainder; i < parts; i++) {
            result[i] = quotient;
        }

        return result;
    }

    public static int rgbToHex(Vec3i rgb) {
        return Integer.parseUnsignedInt(String.format("0x%02X%02X%02X", rgb.getX(), rgb.getY(), rgb.getY()).substring(2), 16);
    }

    public static Vector3f rgbToHsv(Vec3i rgb) {
        return new Vector3f((float) rgb.getX() / 256F, (float) rgb.getY() / 256F, (float) rgb.getZ() / 256F);
    }

    public static int facingToIndex(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> -1;
        };
    }

    public static Direction indexToFacing(int index) {
        return switch (index) {
            case 0 -> Direction.NORTH;
            case 1 -> Direction.EAST;
            case 2 -> Direction.SOUTH;
            case 3 -> Direction.WEST;
            default -> null;
        };
    }

    public static Direction incDirection(Direction facing) {
        int facingIndex = Utils.facingToIndex(facing);
        facingIndex++;
        if (facingIndex > 3)
            return Direction.NORTH;
        return Utils.indexToFacing(facingIndex);
    }

    public static <T> NonNullList<T> listToNonNullList(List<T> list) {
        NonNullList<T> nnl = NonNullList.create();
        nnl.addAll(list);
        return nnl;
    }

    public static <T> T[] listToArray(List<T> list) {
        return (T[]) list.toArray();
    }

    public static <T> void arrayAdd(T[] arr, T elem) {
        arr[arr.length-1] = elem;
    }
}
