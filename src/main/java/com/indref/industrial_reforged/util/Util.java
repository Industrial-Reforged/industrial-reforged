package com.indref.industrial_reforged.util;

import java.util.HashMap;
import java.util.Map;

// class for general and non-minecraft related helper methods
public class Util {
    public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
        Map<V, K> returnMap = new HashMap<>();
        for (K key : map.keySet()) {
            for (V value : map.values()) {
                returnMap.put(value, key);
            }
        }
        return returnMap;
    }
}
