package com.indref.industrial_reforged.api.multiblocks;

public interface DynamicMultiblock extends Multiblock {
    default int[] dynamicLayer(int maxSize, int ...layer) {
        int[] newLayer = new int[layer.length];
        for (int i : layer) {
            newLayer[i] = -i;
        }
        return newLayer;
    }
}
