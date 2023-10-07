package com.indref.industrial_reforged.api.capabilities;

public class EnergyStorage implements IEnergyCapability {
    public final long maxEnergy;
    public long curEnergy;

    // TODO: add different energy properties
    public EnergyStorage(long curEnergy, long maxEnergy) {
        this.curEnergy = curEnergy;
        this.maxEnergy = maxEnergy;
    }

    @Override
    public String toString() {
        return "EnergyStorage(curEnergy: "+curEnergy+", maxEnergy: "+maxEnergy+")";
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
}
