package com.indref.industrial_reforged.content.blocks.pipes;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;

import java.util.function.Supplier;

public class BurntCableBlock extends CableBlock{
    public BurntCableBlock(Properties properties, int width, Supplier<? extends EnergyTier> energyTier) {
        super(properties, width, energyTier);
    }

    @Override
    public EnergyTier getEnergyTier() {
        return null;
    }
}
