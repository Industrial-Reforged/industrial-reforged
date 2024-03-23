package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.ElectricDiggerItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.tags.IRTags;
import net.minecraft.world.item.Tier;

public class RockCutterItem extends ElectricDiggerItem {
    private final EnergyTier energyTier;

    public RockCutterItem(float baseAttackDamage, float attackSpeed, int energyUsage, Tier tier, EnergyTier energyTier, Properties properties) {
        super(baseAttackDamage, attackSpeed, energyUsage, tier, IRTags.Blocks.MINEABLE_WITH_DRILL, properties);
        this.energyTier = energyTier;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }
}
