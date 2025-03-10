package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import com.indref.industrial_reforged.util.ItemUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ElectricHoeItem extends HoeItem implements IEnergyItem {
    public ElectricHoeItem(EnergyTier energyTier, Tier tier, int baseAttackDamage, float baseAttackSpeed, Properties properties) {
        super(tier, properties.stacksTo(1)
                .attributes(HoeItem.createAttributes(tier, baseAttackDamage, baseAttackSpeed))
                .component(IRDataComponents.ENERGY, new ComponentEuStorage(0, energyTier.defaultCapacity())));
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemUtils.energyForDurabilityBar(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemUtils.ENERGY_BAR_COLOR;
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        ItemStack itemInHand = context.getItemInHand();
        BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(context, ItemAbilities.HOE_TILL, false);
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
        IEnergyStorage energyStorage = getEnergyCap(itemInHand);

        if (pair == null) {
            return InteractionResult.PASS;
        } else {
            Player player = context.getPlayer();
            ItemStack itemStack = player.getItemInHand(context.getHand());
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(context) && energyStorage.getEnergyStored() >= 10) {
                level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide()) {
                    consumer.accept(context);
                    energyStorage.tryDrainEnergy(10, false);
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public Holder<EnergyTier> getEnergyTier() {
        return EnergyTiers.LOW;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag p41424) {
        super.appendHoverText(stack, ctx, tooltip, p41424);

        ItemUtils.addEnergyTooltip(tooltip, stack);
    }
}
