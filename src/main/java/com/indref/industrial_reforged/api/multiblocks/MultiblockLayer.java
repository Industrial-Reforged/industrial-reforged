package com.indref.industrial_reforged.api.multiblocks;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.IntegerRange;

import java.util.Arrays;

public record MultiblockLayer(boolean dynamic, IntegerRange range, int[] layer, IntIntPair widths) {
    public MultiblockLayer(boolean dynamic, IntegerRange range, int[] layer) {
        this(dynamic, range, layer, IntIntPair.of((int) Math.sqrt(layer.length), (int) Math.sqrt(layer.length)));
    }

    public MultiblockLayer setDynamic(IntegerRange size) {
        return new MultiblockLayer(true, size, layer, widths);
    }

    public MultiblockLayer setWidths(int xWidth, int zWidth) {
        return new MultiblockLayer(dynamic, range, layer, IntIntPair.of(xWidth, zWidth));
    }

    public static MultiblockLayer load(CompoundTag tag) {
        return new MultiblockLayer(
                tag.getBoolean("dynamic"),
                IntegerRange.of(tag.getInt("rangeMin"), tag.getInt("rangeMax")),
                tag.getIntArray("layer"),
                IntIntPair.of(tag.getInt("widthsX"), tag.getInt("widthsZ"))
        );
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("dynamic", dynamic);
        tag.putInt("rangeMin", range.getMinimum());
        tag.putInt("rangeMax", range.getMaximum());
        tag.putIntArray("layer", layer);
        tag.putInt("widthsX", widths.leftInt());
        tag.putInt("widthsZ", widths.rightInt());
        return tag;
    }

    public IntIntPair getWidths() {
        return widths;
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
