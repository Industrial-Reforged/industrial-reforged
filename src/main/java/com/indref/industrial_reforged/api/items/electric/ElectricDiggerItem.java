package com.indref.industrial_reforged.api.items.electric;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.items.tools.ElectricToolItem;
import com.indref.industrial_reforged.registries.IRDataComponents;
import com.indref.industrial_reforged.registries.data.components.EnergyStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ElectricDiggerItem extends DiggerItem implements IEnergyItem, ElectricToolItem {
    private final int energyUsage;
    private final float mineSpeed;
    private final TagKey<Block> blocks;
    protected final EnergyTier energyTier;

    public ElectricDiggerItem(float baseAttackDamage, float attackSpeed, TagKey<Block> blocks, int energyUsage, EnergyTier energyTier, Tier tier, Properties properties) {
        super(tier, blocks, properties
                .durability(0)
                .attributes(DiggerItem.createAttributes(tier, baseAttackDamage, attackSpeed))
                .component(IRDataComponents.ENERGY, new EnergyStorage(0, energyTier.getDefaultCapacity())));
        this.energyUsage = energyUsage;
        this.blocks = blocks;
        this.energyTier = energyTier;
        this.mineSpeed = tier.getSpeed();
    }

    @Override
    public boolean mineBlock(ItemStack p_41416_, Level p_41417_, BlockState p_41418_, BlockPos p_41419_, LivingEntity p_41420_) {
        IEnergyStorage energyStorage = IEnergyItem.getCap(p_41416_);
        int energyUsage = getEnergyUsage(p_41416_, p_41420_ instanceof Player player ? player : null);
        energyStorage.tryDrainEnergy(energyUsage, false);
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack p_40994_, LivingEntity p_40995_, LivingEntity p_40996_) {
        IEnergyStorage energyStorage = IEnergyItem.getCap(p_40994_);
        int energyUsage = (int) (getEnergyUsage(p_40994_, p_40995_ instanceof Player player ? player : null) * 1.5f);
        energyStorage.tryDrainEnergy(energyUsage, false);
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        int energyUsage = getEnergyUsage(pStack, null);
        return IEnergyItem.getCap(pStack).tryDrainEnergy(energyUsage, true) > 0 ? 12f : 1f;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack p_150901_) {
        return ItemUtils.ENERGY_BAR_COLOR;
    }

    @Override
    public int getBarWidth(ItemStack p_150900_) {
        return ItemUtils.energyForDurabilityBar(p_150900_);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);

        ItemUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public boolean requireEnergyToBreak(ItemStack itemStack, Player player) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Player player) {
        return this.energyUsage;
    }
}
