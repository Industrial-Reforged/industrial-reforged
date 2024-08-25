package com.indref.industrial_reforged.api.blockentities.multiblock;

import com.indref.industrial_reforged.api.multiblocks.MultiblockData;
import net.minecraft.nbt.CompoundTag;

public interface MultiblockEntity {
    MultiblockData getMultiblockData();

    void setMultiblockData(MultiblockData data);

    default CompoundTag saveMBData() {
        return getMultiblockData().serializeNBT();
    }

    default MultiblockData loadMBData(CompoundTag tag) {
        return MultiblockData.deserializeNBT(tag);
    }
}
