package com.indref.industrial_reforged.registries.items.storage;

import com.indref.industrial_reforged.api.items.SimpleElectricItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;

public class BatteryItem extends SimpleElectricItem {
    private final EnergyTier energyTier;
    private final int capacity;

    public BatteryItem(Properties properties, EnergyTier energyTier) {
        super(properties);
        this.energyTier = energyTier;
        this.capacity = energyTier.getDefaultCapacity();
    }

    public BatteryItem(Properties properties, EnergyTier energyTier, int capactiy) {
        super(properties);
        this.energyTier = energyTier;
        this.capacity = capactiy;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    public int getCapacity() {
        return capacity;
    }
}
