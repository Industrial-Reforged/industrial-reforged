package com.indref.industrial_reforged.api.tiers.templates;

import java.util.Map;

public interface EnergyTier {
    int getMaxInput();
    int getMaxOutput();

    // Current flow unit (EU/t)
    int getCurrent();
    int getDefaultCapacity();
    String getName();
}
