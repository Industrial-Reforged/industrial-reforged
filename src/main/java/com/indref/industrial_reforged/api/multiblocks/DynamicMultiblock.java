package com.indref.industrial_reforged.api.multiblocks;

import org.apache.commons.lang3.IntegerRange;

public interface DynamicMultiblock extends Multiblock {
    default MultiblockLayer dynamicLayer(IntegerRange minMaxSize, int ...layer) {
        return new MultiblockLayer(true, minMaxSize, layer);
    }
}
