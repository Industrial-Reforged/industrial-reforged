package com.indref.industrial_reforged.api.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// TODO: 10/7/2023 make a tag for this and consider moving this to an interface

public class ToolItem extends Item implements IToolItem {
    public ToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 100000;
    }
}
