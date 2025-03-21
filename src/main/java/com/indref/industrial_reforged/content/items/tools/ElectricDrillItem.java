package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.items.tools.electric.ElectricDiggerItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class ElectricDrillItem extends ElectricDiggerItem {
    public ElectricDrillItem(Properties properties, float attackSpeed, float baseAttackDamage, Tier tier, Holder<EnergyTier> energyTier, int energyUsage, int energyCapacity) {
        super(properties, attackSpeed, baseAttackDamage, tier, BlockTags.MINEABLE_WITH_PICKAXE, energyTier, energyUsage, energyCapacity);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility) || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility);
    }

}
