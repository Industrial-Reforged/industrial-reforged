package com.indref.industrial_reforged.util;

import com.mojang.datafixers.util.Pair;

import java.util.HashMap;
import java.util.Map;

// class for general and non-minecraft related helper methods
public class Util {
    public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
        Map<V, K> returnMap = new HashMap<>();
        map.forEach((key, value) -> returnMap.put(value, key));
        return returnMap;
    }

    public static final class Coordinates extends Triple<Integer, Integer, Integer> {
        private Coordinates(Integer x, Integer y, Integer z) {
            super(x, y, z);
        }

        public static Coordinates of(int x, int y, int z) {
            return new Coordinates(x, y, z);
        }
    }

    public static final class XZCoordinates extends Pair<Integer, Integer> {
        private XZCoordinates(Integer x, Integer z) {
            super(x, z);
        }

        public static XZCoordinates of(int x, int z) {
            return new XZCoordinates(x, z);
        }
    }

    public static class Triple<A, B, C> {
        private final A a;
        private final B b;
        private final C c;
        private Triple(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public static <A, B, C> Triple<A, B, C> of(A a, B b, C c) {
            return new Triple<>(a, b, c);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+", "+c+")";
        }

        public A getFirst() {
            return this.a;
        }

        public B getSecond() {
            return this.b;
        }

        public C getThird() {
            return this.c;
        }
    }
}
