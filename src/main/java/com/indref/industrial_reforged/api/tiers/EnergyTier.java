package com.indref.industrial_reforged.api.tiers;

import net.minecraft.network.chat.Component;

public interface EnergyTier {
    int getMaxInput();
    int getMaxOutput();

    // Current flow unit (EU/t)
    int getCurrent();
    int getDefaultCapacity();
    Component getName();
}
