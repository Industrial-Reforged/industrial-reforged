package com.indref.industrial_reforged.api.tiers.templates;

public interface EnergyTier {
    int getMaxInput();
    int getMaxOutput();

    // Current flow unit (EU/t)
    int getCurrent();
    int getDefaultCapacity();
}
