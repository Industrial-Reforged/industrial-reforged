package com.indref.industrial_reforged.api.items.electric;

import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.data.components.EnergyStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class ElectricDiggerItem extends DiggerItem implements IEnergyItem {
    private final int energyUsage;
    private final TagKey<Block> blocks;
    protected final EnergyTier energyTier;

    public ElectricDiggerItem(float baseAttackDamage, float attackSpeed, int energyUsage, EnergyTier energyTier, Tier tier, TagKey<Block> mineableBlocks, Properties properties) {
        super(tier, mineableBlocks, properties
                .attributes(DiggerItem.createAttributes(tier, baseAttackDamage, attackSpeed))
                .component(IRDataComponents.ENERGY, new EnergyStorage(0, energyTier.getDefaultCapacity())));
        this.energyUsage = energyUsage;
        this.blocks = mineableBlocks;
        this.energyTier = energyTier;
    }

    public ElectricDiggerItem(float baseAttackDamage, float attackSpeed, EnergyTier energyTier, Tier tier, TagKey<Block> mineableBlocks, Properties properties) {
        this(baseAttackDamage, attackSpeed, 3, energyTier, tier, mineableBlocks, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(this.blocks) && getEnergyStored(pStack) - getEnergyUsage(pStack) > 0 ? super.getDestroySpeed(pStack, pState) : 1f;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level p_40999_, BlockState p_41000_, BlockPos p_41001_, LivingEntity p_41002_) {
        this.tryDrainEnergy(itemStack, getEnergyUsage(itemStack));
        return super.mineBlock(itemStack, p_40999_, p_41000_, p_41001_, p_41002_);
    }

    public int getEnergyUsage(ItemStack itemStack) {
        return energyUsage;
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0;
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
}
