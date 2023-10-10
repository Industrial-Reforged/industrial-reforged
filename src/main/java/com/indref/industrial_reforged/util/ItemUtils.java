package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.energy.items.IEnergyItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

public class ItemUtils {
    // The corresponding rgb value: rgb(77,166,255)
    public static final int ENERGY_BAR_COLOR = 0x4DA6FF;
    public static int energyForDurabilityBar(ItemStack itemStack) {
        IndustrialReforged.LOGGER.info("Energy bar value: "+ Math.round(13.0F - ((1 - getChargeRatio(itemStack)) * 13.0F)));
        IndustrialReforged.LOGGER.info("Charge ratio: "+ getChargeRatio(itemStack));
        itemStack.getCapability(IRCapabilities.ENERGY).ifPresent(energyStorage -> IndustrialReforged.LOGGER.info("Stored energy: "+energyStorage.getEnergyStored()));
        return Math.round(13.0F - ((1 - getChargeRatio(itemStack)) * 13.0F));
    }

    public static IEnergyItem getEnergyItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IEnergyItem energyItem) {
            return energyItem;
        }
        return null;
    }

    public static float getChargeRatio(ItemStack stack) {
        IEnergyStorage energyStorage = stack.getCapability(IRCapabilities.ENERGY).orElseThrow(NullPointerException::new);
        return (float) energyStorage.getEnergyStored() / energyStorage.getMaxEnergy();
    }
}
