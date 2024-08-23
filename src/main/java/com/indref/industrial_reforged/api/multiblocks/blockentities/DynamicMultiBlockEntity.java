package com.indref.industrial_reforged.api.multiblocks.blockentities;

import com.indref.industrial_reforged.api.multiblocks.MultiblockLayer;
import net.minecraft.nbt.CompoundTag;

public interface DynamicMultiBlockEntity {
    MultiblockLayer[] getExpandedLayers();

    void setExpandedLayers(MultiblockLayer[] layers);

    default CompoundTag saveDynamicMulti() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("layersLength", getExpandedLayers().length);
        CompoundTag listTag = new CompoundTag();
        MultiblockLayer[] expandedLayers = getExpandedLayers();
        for (int i = 0, expandedLayersLength = expandedLayers.length; i < expandedLayersLength; i++) {
            MultiblockLayer layer = expandedLayers[i];
            listTag.put(String.valueOf(i), layer.save());
        }
        tag.put("layersList", listTag);
        return tag;
    }

    default MultiblockLayer[] loadDynamicMulti(CompoundTag tag) {
        int layersLength = tag.getInt("layersLength");
        CompoundTag listTag = tag.getCompound("layersList");
        MultiblockLayer[] layers = new MultiblockLayer[layersLength];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = MultiblockLayer.load(listTag.getCompound(String.valueOf(i)));
        }
        return layers;
    }
}
