package com.indref.industrial_reforged.api.multiblocks;

import com.indref.industrial_reforged.api.util.HorizontalDirection;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

public class MultiblockData implements INBTSerializable<CompoundTag> {
    public static final MultiblockData EMPTY = new MultiblockData(false, HorizontalDirection.NORTH, new MultiblockLayer[0]);

    private boolean valid;
    private HorizontalDirection direction;
    private MultiblockLayer[] layers;

    public MultiblockData(boolean valid, HorizontalDirection direction, MultiblockLayer[] layers) {
        this.valid = valid;
        this.direction = direction;
        this.layers = layers;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("layersLength", layers.length);
        CompoundTag listTag = new CompoundTag();
        for (int i = 0, expandedLayersLength = layers.length; i < expandedLayersLength; i++) {
            MultiblockLayer layer = layers[i];
            listTag.put(String.valueOf(i), layer.save());
        }
        tag.put("layersList", listTag);
        tag.putInt("direction", this.direction.ordinal());
        tag.putBoolean("valid", this.valid);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        int layersLength = nbt.getInt("layersLength");
        CompoundTag listTag = nbt.getCompound("layersList");
        MultiblockLayer[] layers = new MultiblockLayer[layersLength];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = MultiblockLayer.load(listTag.getCompound(String.valueOf(i)));
        }
        HorizontalDirection direction = HorizontalDirection.values()[nbt.getInt("direction")];
        boolean valid = nbt.getBoolean("valid");
        this.valid = valid;
        this.direction = direction;
        this.layers = layers;
    }

    public boolean valid() {
        return valid;
    }

    public HorizontalDirection direction() {
        return direction;
    }

    public MultiblockLayer[] layers() {
        return layers;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MultiblockData) obj;
        return this.valid == that.valid &&
                Objects.equals(this.direction, that.direction) &&
                Objects.equals(this.layers, that.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valid, direction, layers);
    }

    @Override
    public String toString() {
        return "MultiblockData[" +
                "valid=" + valid + ", " +
                "direction=" + direction + ", " +
                "layers=" + layers + ']';
    }

}