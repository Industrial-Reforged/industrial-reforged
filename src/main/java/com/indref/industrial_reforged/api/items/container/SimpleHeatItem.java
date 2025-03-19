package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentHeatStorage;
import com.indref.industrial_reforged.util.TooltipUtils;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import com.indref.industrial_reforged.util.items.ItemUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleHeatItem extends Item implements IHeatItem {
    public SimpleHeatItem(Item.Properties properties) {
        super(properties.component(IRDataComponents.HEAT.get(), ComponentHeatStorage.EMPTY));
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemBarUtils.heatBarWidth(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemBarUtils.heatBarColor(itemStack);
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
