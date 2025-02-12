package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.items.tools.electric.ElectricDiggerItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.data.IRDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class ElectricChainsawItem extends ElectricDiggerItem {
    public ElectricChainsawItem(float baseAttackDamage, float attackSpeed, int energyUsage, Holder<EnergyTier> energyTier, Tier tier, Properties properties) {
        super(baseAttackDamage, attackSpeed, BlockTags.MINEABLE_WITH_AXE, energyUsage, energyTier, tier, properties.component(IRDataComponents.ACTIVE, false));
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (entity instanceof Player player) {
            ItemStack itemInHand = player.getMainHandItem();

            if (itemInHand.is(this)) {
                if (!stack.get(IRDataComponents.ACTIVE)) {
                    stack.set(IRDataComponents.ACTIVE, true);
                }
            } else {
                if (stack.get(IRDataComponents.ACTIVE)) {
                    stack.set(IRDataComponents.ACTIVE, false);
                }
            }
        }
    }

}
