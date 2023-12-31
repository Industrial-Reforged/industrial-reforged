package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleHeatItem extends Item implements IHeatItem {
    public SimpleHeatItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemUtils.heatForDurabilityBar(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemUtils.HEAT_BAR_COLOR;
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 1;
    }
}
