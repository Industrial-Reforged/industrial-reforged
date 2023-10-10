package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.energy.items.IEnergyItem;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleElectricItem extends Item implements IEnergyItem {
    public SimpleElectricItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemUtils.energyForDurabilityBar(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemUtils.ENERGY_BAR_COLOR;
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }
}
