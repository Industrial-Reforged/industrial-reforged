package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.items.container.IFluidContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.ItemFluidContainer;

public abstract class SimpleFluidItem extends ItemFluidContainer implements IFluidContainerItem {
    public SimpleFluidItem(Properties properties, int capacity) {
        super(properties, capacity);
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 1;
    }
}
