package com.indref.industrial_reforged.registries.items.tools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// TODO: 10/7/2023 make a tag for this as well

/**
 * This is a class that implements the standard {@link com.indref.industrial_reforged.api.items.ToolItem}
 * interface and overrides a few methods that all tool items should override
 */
public class ToolItem extends Item implements com.indref.industrial_reforged.api.items.ToolItem {
    public ToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity p_344979_) {
        return 1;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}
