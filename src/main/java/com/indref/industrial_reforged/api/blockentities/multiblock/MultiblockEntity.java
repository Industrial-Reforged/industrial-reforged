package com.indref.industrial_reforged.api.blockentities.multiblock;

import com.indref.industrial_reforged.api.multiblocks.MultiblockData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public interface MultiblockEntity {
    MultiblockData getMultiblockData();

    void setMultiblockData(MultiblockData data);

    default CompoundTag saveMBData(HolderLookup.Provider lookup) {
        return getMultiblockData().serializeNBT(lookup);
    }

    default void loadMBData(HolderLookup.Provider lookup, CompoundTag tag) {
        getMultiblockData().deserializeNBT(lookup, tag);
    }
}
