package com.indref.industrial_reforged.api.multiblocks;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import org.apache.commons.lang3.IntegerRange;

import java.util.Arrays;

public record MultiblockLayer(boolean dynamic, IntegerRange range, int[] layer) {
    public IntIntPair getWidths() {
        int width = (int) Math.sqrt(layer.length);
        return IntIntPair.of(width, width);
    }

    @Override
    public String toString() {
        return "MultiblockLayer{" +
                "dynamic=" + dynamic +
                ", range=" + range +
                ", layer=" + Arrays.toString(layer) +
                '}';
    }
}
