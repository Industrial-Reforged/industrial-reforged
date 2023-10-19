package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.api.items.SimpleHeatItem;
import net.minecraft.world.item.ItemStack;

public class HeatTestItem extends SimpleHeatItem {
    public HeatTestItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getCapacity(ItemStack itemStack) {
        return 10000;
    }
}
