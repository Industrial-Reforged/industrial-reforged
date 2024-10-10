package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.electric.ElectricDiggerItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RockCutterItem extends ElectricDiggerItem {
    private final EnergyTier energyTier;

    public RockCutterItem(float baseAttackDamage, float attackSpeed, EnergyTier energyTier, Tier tier, Properties properties) {
        super(baseAttackDamage, attackSpeed, BlockTags.MINEABLE_WITH_PICKAXE, energyTier.getMaxOutput() / 2, energyTier, tier, properties);
        this.energyTier = energyTier;
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        Optional<Holder.Reference<Enchantment>> optionalEnchantmentReference = pLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.SILK_TOUCH);
        optionalEnchantmentReference.ifPresent(enchantmentReference -> pStack.enchant(enchantmentReference, 1));
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }
}
