package com.indref.industrial_reforged.api.blocks;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import org.jetbrains.annotations.Nullable;

public interface EnergyTierBlock {
    @Nullable EnergyTier getEnergyTier();
}
