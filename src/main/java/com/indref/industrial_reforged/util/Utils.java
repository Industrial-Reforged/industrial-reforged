package com.indref.industrial_reforged.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Utils {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
        Map<V, K> returnMap = new HashMap<>();
        map.forEach((key, value) -> returnMap.put(value, key));
        return returnMap;
    }

    public static <T extends BlockEntity & MenuProvider> void openMenu(Player player, T blockEntity) {
        if (blockEntity != null) {
            player.openMenu(blockEntity, blockEntity.getBlockPos());
        }
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

    public static <T> NonNullList<T> listToNonNullList(List<T> list) {
        NonNullList<T> nnl = NonNullList.create();
        nnl.addAll(list);
        return nnl;
    }

    public static IntList intArrayToList(int[] array) {
        IntList list = new IntArrayList();
        for (int i : array) {
            list.add(i);
        }
        return list;
    }
}
