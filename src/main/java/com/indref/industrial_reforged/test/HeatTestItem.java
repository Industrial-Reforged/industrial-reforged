package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.api.items.SimpleHeatItem;
import net.minecraft.world.item.ItemStack;

public class HeatTestItem extends SimpleHeatItem {
    public HeatTestItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getCapacity() {
        return 10000;
    }
}
