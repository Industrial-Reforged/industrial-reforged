package com.indref.industrial_reforged.api.tiers;

import net.minecraft.resources.ResourceLocation;

public interface EnergyTier {
    ResourceLocation getId();

    int getMaxInput();
    int getMaxOutput();

    int getDefaultCapacity();
    int getColor();
}
