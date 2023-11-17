package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.IToolItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// TODO: 10/7/2023 make a tag for this and consider moving this to an interface

public class ToolItem extends Item implements IToolItem {
    public ToolItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 1;
    }
}
