package com.indref.industrial_reforged.api.items.electric;

import com.indref.industrial_reforged.api.items.tools.ElectricToolItem;
import com.indref.industrial_reforged.registries.IRDataComponents;
import com.indref.industrial_reforged.registries.data.components.EnergyStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public abstract class ElectricSwordItem extends SwordItem implements IEnergyItem, ElectricToolItem {
    protected final EnergyTier energyTier;

    public ElectricSwordItem(EnergyTier energyTier, Tier tier, int baseAttackDamage, float baseAttackSpeed, Properties properties) {
        super(tier, properties
                .attributes(SwordItem.createAttributes(tier, baseAttackDamage, baseAttackSpeed))
                .component(IRDataComponents.ENERGY, new EnergyStorage(0, energyTier.getDefaultCapacity())));
        this.energyTier = energyTier;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
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
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity p_344979_) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext p_339594_, List<Component> tooltip, TooltipFlag p_41424_) {
        super.appendHoverText(stack, p_339594_, tooltip, p_41424_);
        ItemUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public boolean requireEnergyToBreak(ItemStack itemStack, Player player) {
        return true;
    }
}
