package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ElectricDiggerItem extends DiggerItem implements IEnergyItem {
    private final int energyUsage;
    private final TagKey<Block> blocks;

    public ElectricDiggerItem(float baseAttackDamage, float attackSpeed, int energyUsage, Tier tier, TagKey<Block> mineableBlocks, Properties properties) {
        super(baseAttackDamage, attackSpeed, tier, mineableBlocks, properties);
        this.energyUsage = energyUsage;
        this.blocks = mineableBlocks;
    }

    public ElectricDiggerItem(float baseAttackDamage, float attackSpeed, Tier tier, TagKey<Block> mineableBlocks, Properties properties) {
        super(baseAttackDamage, attackSpeed, tier, mineableBlocks, properties);
        this.energyUsage = -1;
        this.blocks = mineableBlocks;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(this.blocks) && getEnergyStored(pStack) - getEnergyUsage(pStack) > 0 ? this.speed : 1f;
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

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return false;
    }

    public int getEnergyUsage(ItemStack itemStack) {
        return energyUsage != -1 ? energyUsage : 3;
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
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        ItemUtils.addEnergyTooltip(tooltip, p_41421_);
    }
}
