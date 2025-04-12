package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.items.tools.electric.ElectricDiggerItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ElectricChainsawItem extends ElectricDiggerItem {

    public ElectricChainsawItem(Properties properties, float attackSpeed, float baseAttackDamage, Tier tier, Supplier<EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
        super(properties, attackSpeed, baseAttackDamage, tier, BlockTags.MINEABLE_WITH_AXE, energyTier, energyUsage, defaultEnergyCapacity);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility);
    }

}
