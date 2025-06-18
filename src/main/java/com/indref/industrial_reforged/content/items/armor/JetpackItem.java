package com.indref.industrial_reforged.content.items.armor;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.util.items.TooltipUtils;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Supplier;

public class JetpackItem extends ArmorItem implements IEnergyItem {
    private static final int STAGES = 6;
    private final Supplier<EnergyTier> energyTierSupplier;

    public JetpackItem(Properties properties, Holder<ArmorMaterial> material, Supplier<EnergyTier> energyTierSupplier) {
        super(material, Type.CHESTPLATE, properties.stacksTo(1).durability(0).component(IRDataComponents.ENERGY, ComponentEuStorage.EMPTY));
        this.energyTierSupplier = energyTierSupplier;
    }

    public float getJetpackStage(ItemStack stack) {
        IEnergyStorage energyStorage = getEnergyCap(stack);
        return ((float) energyStorage.getEnergyStored() / energyStorage.getEnergyCapacity()) * (STAGES - 1);
    }

    // TODO: Create config
    @Override
    public int getDefaultEnergyCapacity() {
        return 4000;
    }

    @Override
    public Supplier<EnergyTier> getEnergyTier() {
        return energyTierSupplier;
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
}
