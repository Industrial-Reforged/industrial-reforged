package com.indref.industrial_reforged.registries.items.storage;

import com.indref.industrial_reforged.api.items.SimpleElectricItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.world.item.ItemStack;

public class BatteryItem extends SimpleElectricItem {
    public static final String ENERGY_STAGE_KEY = "energy_stage";

    private final EnergyTier energyTier;
    private final int capacity;

    public BatteryItem(Properties properties, EnergyTier energyTier) {
        super(properties);
        this.energyTier = energyTier;
        this.capacity = energyTier.getDefaultCapacity();
    }

    public BatteryItem(Properties properties, EnergyTier energyTier, int capacity) {
        super(properties);
        this.energyTier = energyTier;
        this.capacity = capacity;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public void onEnergyChanged(ItemStack itemStack) {
        float energyPercentage = (float) getEnergyStored(itemStack) / getCapacity();
        itemStack.getOrCreateTag().putFloat(ENERGY_STAGE_KEY, (int) (energyPercentage * 5));
    }

    public static float getEnergyStage(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getFloat(ENERGY_STAGE_KEY);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return false;
    }
}
