package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.energy.items.IEnergyItem;
import net.minecraft.world.item.Item;

public abstract class SimpleElectricItem extends Item implements IEnergyItem {
    public SimpleElectricItem(Properties properties) {
        super(properties);
    }
}
