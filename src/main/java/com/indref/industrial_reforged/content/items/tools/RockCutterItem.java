package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.items.tools.electric.ElectricDiggerItem;
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
    private final int energyCapacity;

    public RockCutterItem(Properties properties, float attackSpeed, float baseAttackDamage, Tier tier, Holder<EnergyTier> energyTier, int energyUsage, int defaultEnergyCapacity) {
        super(properties, attackSpeed, baseAttackDamage, tier, BlockTags.MINEABLE_WITH_PICKAXE, energyTier, energyUsage, defaultEnergyCapacity);
        this.energyCapacity = defaultEnergyCapacity;
    }

    @Override
    public int getDefaultEnergyCapacity() {
        return this.energyCapacity;
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        Optional<Holder.Reference<Enchantment>> optionalEnchantmentReference = pLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.SILK_TOUCH);
        optionalEnchantmentReference.ifPresent(enchantmentReference -> pStack.enchant(enchantmentReference, 1));
    }

}
