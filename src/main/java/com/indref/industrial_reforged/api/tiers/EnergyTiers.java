package com.indref.industrial_reforged.api.tiers;

import com.indref.industrial_reforged.api.tiers.templates.EnergyTier;

public enum EnergyTiers implements EnergyTier {
    LOW(32, 32, 32_000),
    MEDIUM(64, 64, 128_000),
    HIGH(128, 128, 400_000),
    EXTREME(512, 512, 4_000_000),
    INSANE(1024, 1024, 40_000_000),
    CREATIVE(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int maxInput;
    private final int maxOutput;
    private final int current;
    private final int defaultCapacity;

    EnergyTiers(int maxInput, int maxOutput, int current, int defaultCapacity) {
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
        this.current = current;
        this.defaultCapacity = defaultCapacity;
    }

    EnergyTiers(int throughPut, int current, int defaultCapacity) {
        this.maxInput = throughPut;
        this.maxOutput = throughPut;
        this.current = current;
        this.defaultCapacity = defaultCapacity;
    }

    @Override
    public int getMaxInput() {
        return this.maxInput;
    }

    @Override
    public int getMaxOutput() {
        return this.maxOutput;
    }

    @Override
    public int getCurrent() {
        return this.current;
    }

    @Override
    public int getDefaultCapacity() {
        return this.defaultCapacity;
    }
}
