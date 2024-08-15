package com.indref.industrial_reforged.api.multiblocks;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.IntegerRange;

import java.util.Arrays;

public record MultiblockLayer(boolean dynamic, IntegerRange range, int[] layer) {
    public static MultiblockLayer load(CompoundTag tag) {
        return new MultiblockLayer(
                tag.getBoolean("dynamic"),
                IntegerRange.of(tag.getInt("rangeMin"), tag.getInt("rangeMax")),
                tag.getIntArray("layer")
        );
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("dynamic", dynamic);
        tag.putInt("rangeMin", range.getMinimum());
        tag.putInt("rangeMax", range.getMaximum());
        tag.putIntArray("layer", layer);
        return tag;
    }

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
