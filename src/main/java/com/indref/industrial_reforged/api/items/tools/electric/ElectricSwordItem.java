package com.indref.industrial_reforged.api.items.tools.electric;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;
import com.indref.industrial_reforged.util.items.TooltipUtils;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public abstract class ElectricSwordItem extends SwordItem implements IEnergyItem, ElectricToolItem {
    protected final Supplier<EnergyTierImpl> energyTier;
    private final IntSupplier energyUsage;
    private final IntSupplier energyCapacity;

    public ElectricSwordItem(Properties properties, Tier tier, int baseAttackDamage, float baseAttackSpeed, Supplier<EnergyTierImpl> energyTier, IntSupplier energyUsage, IntSupplier energyCapacity) {
        super(tier, properties
                .attributes(SwordItem.createAttributes(tier, baseAttackDamage, baseAttackSpeed))
                .component(IRDataComponents.ENERGY, new ComponentEuStorage(0, energyCapacity.getAsInt())));
        this.energyTier = energyTier;
        this.energyUsage = energyUsage;
        this.energyCapacity = energyCapacity;
    }

    public int getEnergyUsage() {
        return energyUsage.getAsInt();
    }

    @Override
    public int getDefaultCapacity() {
        return energyCapacity.getAsInt();
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return getEnergyUsage();
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
    public boolean requireEnergyToWork(ItemStack itemStack, Entity player) {
        return true;
    }
}
