package com.indref.industrial_reforged.capabilities.energy;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Class for saving and loading energy values of item/block/entity
 * <br><br>
 * Should not be use directly,
 * only for attaching data/accessing it through capabilities
 */
public class EnergyStorage implements INBTSerializable<Tag> {
    private int energy;

    public EnergyStorage(int energy) {
        this.energy = energy;
    }

    public int getEnergyStored() {
        return energy;
    }

    public void setEnergyStored(int val) {
        this.energy = val;
    }

    @Override
    public Tag serializeNBT() {
        return IntTag.valueOf(this.getEnergyStored());
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (!(nbt instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energy = intNbt.getAsInt();
    }
}
