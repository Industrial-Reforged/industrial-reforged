package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentHeatStorage;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleHeatItem extends Item implements IHeatItem {
    public SimpleHeatItem(Item.Properties properties) {
        super(properties.component(IRDataComponents.HEAT.get(), ComponentHeatStorage.EMPTY));
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
    public int getUseDuration(ItemStack stack, LivingEntity p_344979_) {
        return 1;
    }
}
