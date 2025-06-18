package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.items.TooltipUtils;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public abstract class SimpleEnergyItem extends Item implements IEnergyItem {
    protected final IntSupplier defaultEnergyCapacity;
    private final Supplier<EnergyTier> energyTier;

    public SimpleEnergyItem(Properties properties, Supplier<EnergyTier> energyTier, IntSupplier defaultEnergyCapacity) {
        super(properties.stacksTo(1).component(IRDataComponents.ENERGY.get(), new ComponentEuStorage(defaultEnergyCapacity.getAsInt())));
        this.defaultEnergyCapacity = defaultEnergyCapacity;
        this.energyTier = energyTier;
    }

    @Override
    public int getDefaultEnergyCapacity() {
        return defaultEnergyCapacity.getAsInt();
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
    public Supplier<EnergyTier> getEnergyTier() {
        return energyTier;
    }
}
