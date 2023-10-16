package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.api.items.container.IHeatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HeatTestItem extends Item implements IHeatItem {
    public HeatTestItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int getCapacity(ItemStack itemStack) {
        return 10000;
    }
}
