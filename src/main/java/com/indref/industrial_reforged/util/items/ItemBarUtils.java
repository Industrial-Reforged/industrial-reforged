package com.indref.industrial_reforged.util.items;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;

public final class ItemBarUtils {
    public static int energyBarWidth(ItemStack stack) {
        IEnergyStorage energyStorage = stack.getCapability(IRCapabilities.EnergyStorage.ITEM);
        float ratio = (float) energyStorage.getEnergyStored() / energyStorage.getEnergyCapacity();
        return Math.round(13.0F - ((1 - ratio) * 13.0F));
    }

    public static int heatBarWidth(ItemStack stack) {
        IHeatStorage heatStorage = stack.getCapability(IRCapabilities.HeatStorage.ITEM);
        if (heatStorage != null) {
            float ratio = heatStorage.getHeatStored() / heatStorage.getHeatCapacity();
            return Math.round(13.0F - ((1 - ratio) * 13.0F));
        }
        return 0;
    }

    public static int energyBarColor(ItemStack stack) {
        return FastColor.ARGB32.color(255, 215, 0, 0);
    }

    public static int heatBarColor(ItemStack stack) {
        return FastColor.ARGB32.color(255, 255, 128, 6);
    }
}
