package com.indref.industrial_reforged.api.tiers;

public interface EnergyTier {
    int getMaxInput();
    int getMaxOutput();

    // Current flow unit (EU/t)
    int getCurrent();
    int getDefaultCapacity();
    String getId();
    int getColor();
}
