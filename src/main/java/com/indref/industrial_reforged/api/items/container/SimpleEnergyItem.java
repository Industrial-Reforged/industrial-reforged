package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.TooltipUtils;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import com.indref.industrial_reforged.util.items.ItemUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public abstract class SimpleEnergyItem extends Item implements IEnergyItem {
    private final Holder<EnergyTier> energyTier;

    public SimpleEnergyItem(Properties properties, Holder<EnergyTier> energyTier) {
        super(properties.stacksTo(1).component(IRDataComponents.ENERGY.get(), new ComponentEuStorage(0, energyTier.value().defaultCapacity())));
        this.energyTier = energyTier;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemBarUtils.energyBarWidth(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemBarUtils.energyBarColor(itemStack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity p_344979_) {
        return 1;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        TooltipUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public Holder<EnergyTier> getEnergyTier() {
        return energyTier;
    }
}
