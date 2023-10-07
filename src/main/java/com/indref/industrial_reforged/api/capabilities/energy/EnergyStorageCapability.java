package com.indref.industrial_reforged.api.capabilities.energy;

import net.minecraft.nbt.CompoundTag;

public class EnergyStorageCapability implements IEnergyStorageCapability {
    public EnergyStorageCapability(long curEnergy, long maxEnergy) {

    }

    // TODO: 10/7/2023 Change visibility of these 
    public long maxEnergy;
    public long curEnergy;

    private static final String NBT_KEY_CUR_ENERGY = "curEnergy";

    // TODO: 10/7/2023 Add different energy properties

    @Override
    public String toString() {
        return "EnergyStorage(curEnergy: " + curEnergy + ", maxEnergy: " + maxEnergy + ")";
    }

    @Override
    public long getMaxEnergy() {
        return maxEnergy;
    }

    @Override
    public long getCurEnergy() {
        return curEnergy;
    }

    @Override
    public void setCurEnergy(long value) {
        this.curEnergy = value;
    }

    @Override
    public void increaseCurEnergy(long value) {
        this.curEnergy += value;
    }

    @Override
    public void decreaseCurEnergy(long value) {
        this.curEnergy -= value;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putLong(NBT_KEY_CUR_ENERGY, this.curEnergy);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.curEnergy = nbt.getLong(NBT_KEY_CUR_ENERGY);
    }
}
