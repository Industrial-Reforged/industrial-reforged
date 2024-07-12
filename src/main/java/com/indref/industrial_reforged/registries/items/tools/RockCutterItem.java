package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.electric.ElectricDiggerItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class RockCutterItem extends ElectricDiggerItem {
    private final EnergyTier energyTier;

    public RockCutterItem(float baseAttackDamage, float attackSpeed, int energyUsage, EnergyTier energyTier, Tier tier, Properties properties) {
        super(baseAttackDamage, attackSpeed, energyUsage, energyTier, tier, IRTags.Blocks.MINEABLE_WITH_DRILL, properties);
        this.energyTier = energyTier;
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        //pStack.enchant(Enchantments.SILK_TOUCH, 1);
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }
}
