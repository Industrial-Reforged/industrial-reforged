package com.indref.industrial_reforged.api.items.container;

import com.indref.industrial_reforged.registries.IRDataComponents;
import com.indref.industrial_reforged.registries.data.components.EnergyStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.items.tools.ScannerItem;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public abstract class SimpleElectricItem extends Item implements IEnergyItem {
    private final EnergyTier energyTier;

    public SimpleElectricItem(Properties properties, EnergyTier energyTier) {
        super(properties.stacksTo(1).component(IRDataComponents.ENERGY.get(), new EnergyStorage(0, energyTier.getDefaultCapacity())));
        this.energyTier = energyTier;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemUtils.energyForDurabilityBar(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemUtils.ENERGY_BAR_COLOR;
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
        int x = switch (stack.getItem()) {
            case ScannerItem scannerItem -> 0;
            default -> 0;
        };
        ItemUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier;
    }
}
