package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.api.tiers.EnergyTier;

public enum EnergyTiers implements EnergyTier {
    LOW("low", 32, 32, 32_000),
    MEDIUM("medium",64, 64, 128_000),
    HIGH("high",128, 128, 400_000),
    EXTREME("extreme", 512, 512, 4_000_000),
    INSANE("insane", 1024, 1024, 40_000_000),
    CREATIVE("creative", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int maxInput;
    private final int maxOutput;
    private final int current;
    private final int defaultCapacity;
    private final String name;

    EnergyTiers(String name, int maxInput, int maxOutput, int current, int defaultCapacity) {
        this.name = name;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
        this.current = current;
        this.defaultCapacity = defaultCapacity;
    }

    EnergyTiers(String name, int throughPut, int current, int defaultCapacity) {
        this.name = name;
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

    @Override
    public String getName() {
        return this.name;
    }
}
