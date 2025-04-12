package com.indref.industrial_reforged.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    record CollectorImpl<T, A, R>(Supplier<A> supplier,
                                  BiConsumer<A, T> accumulator,
                                  BinaryOperator<A> combiner,
                                  Function<A, R> finisher,
                                  Set<Characteristics> characteristics
    ) implements Collector<T, A, R> {

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }
    }

    private static IllegalStateException duplicateKeyException(
            Object k, Object u, Object v) {
        return new IllegalStateException(String.format(
                "Duplicate key %s (attempted merging values %s and %s)",
                k, u, v));
    }

    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    private static <T, K, V> BiConsumer<Map<K, V>, T> uniqKeysMapAccumulator(Function<? super T, ? extends K> keyMapper,
                                                    Function<? super T, ? extends V> valueMapper) {
        return (map, element) -> {
            K k = keyMapper.apply(element);
            V v = Objects.requireNonNull(valueMapper.apply(element));
            V u = map.putIfAbsent(k, v);
            if (u != null) throw duplicateKeyException(k, u, v);
        };
    }

    private static <K, V, M extends Map<K,V>> BinaryOperator<M> uniqKeysMapMerger() {
        return (m1, m2) -> {
            for (Map.Entry<K,V> e : m2.entrySet()) {
                K k = e.getKey();
                V v = Objects.requireNonNull(e.getValue());
                V u = m1.putIfAbsent(k, v);
                if (u != null) throw duplicateKeyException(k, u, v);
            }
            return m1;
        };
    }

    public static <T, K, U>
    Collector<T, ?, HashMap<K,U>> toHashMap(Function<? super T, ? extends K> keyMapper,
                                    Function<? super T, ? extends U> valueMapper) {
        return new CollectorImpl<>(HashMap::new,
                uniqKeysMapAccumulator(keyMapper, valueMapper),
                uniqKeysMapMerger(),
                CH_ID);
    }

}
