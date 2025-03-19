package com.indref.industrial_reforged.api.items.tools.electric;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.TooltipUtils;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import com.indref.industrial_reforged.util.items.ItemUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public abstract class ElectricSwordItem extends SwordItem implements IEnergyItem, ElectricToolItem {
    protected final Holder<EnergyTier> energyTier;

    public ElectricSwordItem(Holder<EnergyTier> energyTier, Tier tier, int baseAttackDamage, float baseAttackSpeed, Properties properties) {
        super(tier, properties
                .attributes(SwordItem.createAttributes(tier, baseAttackDamage, baseAttackSpeed))
                .component(IRDataComponents.ENERGY, new ComponentEuStorage(0, energyTier.value().defaultCapacity())));
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
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemBarUtils.energyBarColor(itemStack);
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemBarUtils.energyBarWidth(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);

        TooltipUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity p_344979_) {
        return 1;
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, Player player) {
        return true;
    }
}
