package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.items.electric.ElectricDiggerItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class ElectricDrillItem extends ElectricDiggerItem {
    private final EnergyTier energyTier;

    public ElectricDrillItem(float p_204108_, float p_204109_, int energyUsage, EnergyTier energyTier, Tier tier, Properties p_204112_) {
        super(p_204108_, p_204109_, BlockTags.MINEABLE_WITH_PICKAXE, energyUsage, energyTier, tier, p_204112_);
        this.energyTier = energyTier;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility) || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility);
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier;
    }
}
