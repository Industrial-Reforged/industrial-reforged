package com.indref.industrial_reforged.api.data.heat;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Class for saving and loading heat values of item/block/entity
 * <br><br>
 * Should not be use directly,
 * only for attaching data/accessing it through capabilities
 */
public class HeatStorage implements INBTSerializable<Tag> {
    private int heat;

    public HeatStorage(int heat) {
        this.heat = heat;
    }

    public int getHeatStored() {
        return heat;
    }

    @Override
    public Tag serializeNBT() {
        return IntTag.valueOf(this.getHeatStored());
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (!(nbt instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.heat = intNbt.getAsInt();
    }
}
