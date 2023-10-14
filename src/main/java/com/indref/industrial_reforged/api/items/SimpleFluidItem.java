package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.items.container.IFluidContainerItem;
import net.minecraftforge.fluids.capability.ItemFluidContainer;

public abstract class SimpleFluidItem extends ItemFluidContainer implements IFluidContainerItem {
    public SimpleFluidItem(Properties properties, int capacity) {
        super(properties, capacity);
    }
}
