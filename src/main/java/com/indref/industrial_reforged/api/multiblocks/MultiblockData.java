package com.indref.industrial_reforged.api.multiblocks;

import com.indref.industrial_reforged.api.util.HorizontalDirection;
import net.minecraft.nbt.CompoundTag;

public record MultiblockData(boolean valid, HorizontalDirection direction, MultiblockLayer[] layers) {
    public static final MultiblockData EMPTY = new MultiblockData(false, HorizontalDirection.NORTH, new MultiblockLayer[0]);

    public CompoundTag serializeNBT() {
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

    public static MultiblockData deserializeNBT(CompoundTag nbt) {
        int layersLength = nbt.getInt("layersLength");
        CompoundTag listTag = nbt.getCompound("layersList");
        MultiblockLayer[] layers = new MultiblockLayer[layersLength];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = MultiblockLayer.load(listTag.getCompound(String.valueOf(i)));
        }
        HorizontalDirection direction = HorizontalDirection.values()[nbt.getInt("direction")];
        boolean valid = nbt.getBoolean("valid");
        return new MultiblockData(valid, direction, layers);
    }
}