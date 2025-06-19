package com.indref.industrial_reforged.api.items.tools.electric;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.items.TooltipUtils;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
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
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public abstract class ElectricDiggerItem extends DiggerItem implements IEnergyItem, ElectricToolItem {
    private final float baseAttackDamage;
    private final float attackSpeed;
    private final Tier tier;
    @org.jetbrains.annotations.NotNull
    private final TagKey<Block> blocks;
    protected final Supplier<EnergyTier> energyTier;
    private final IntSupplier energyUsage;
    private final IntSupplier defaultEnergyCapacity;

    public ElectricDiggerItem(Properties properties, float attackSpeed, float baseAttackDamage, Tier tier, TagKey<Block> blocks, Supplier<EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
        super(tier, blocks, properties
                .durability(0)
                .attributes(DiggerItem.createAttributes(tier, baseAttackDamage, attackSpeed))
                .component(IRDataComponents.ENERGY, new ComponentEuStorage(defaultEnergyCapacity.getAsInt())));
        this.baseAttackDamage = baseAttackDamage;
        this.attackSpeed = attackSpeed;
        this.energyUsage = energyUsage;
        this.blocks = blocks;
        this.energyTier = energyTier;
        this.tier = tier;
        this.defaultEnergyCapacity = defaultEnergyCapacity;
    }

    @Override
    public void initEnergyStorage(IEnergyStorage energyStorage, ItemStack itemStack) {
        if (energyStorage.getEnergyStored() == 0) {
            itemStack.remove(DataComponents.TOOL);
        } else {
            itemStack.set(DataComponents.TOOL, tier.createToolProperties(this.blocks));
        }
    }

    @Override
    public void onEnergyChanged(ItemStack itemStack, int oldAmount) {
        IEnergyStorage energyStorage = itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);

        itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, DiggerItem.createAttributes(
                tier,
                !requireEnergyToWork(itemStack, null)
                        || energyStorage.getEnergyStored() >= getEnergyUsage(itemStack, null)
                        ? baseAttackDamage
                        : 0,
                attackSpeed
        ));
        if (energyStorage.getEnergyStored() == 0) {
            itemStack.remove(DataComponents.TOOL);
        } else if (oldAmount == 0) {
            itemStack.set(DataComponents.TOOL, tier.createToolProperties(this.blocks));
        }
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level p_41417_, BlockState p_41418_, BlockPos p_41419_, LivingEntity entity) {
        Player player = entity instanceof Player player0 ? player0 : null;
        if (canWork(stack, player)) {
            IEnergyStorage energyStorage = getEnergyCap(stack);
            int energyUsage = getEnergyUsage(stack, player);
            energyStorage.tryDrainEnergy(energyUsage, false);
        }
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity p_40995_, LivingEntity entity) {
        Player player = entity instanceof Player player0 ? player0 : null;
        if (canWork(stack, player)) {
            IEnergyStorage energyStorage = getEnergyCap(stack);
            int energyUsage = (int) (getEnergyUsage(stack, player) * 1.5f);
            energyStorage.tryDrainEnergy(energyUsage, false);
        }
        return true;
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
    public int getBarColor(ItemStack itemStack) {
        return ItemBarUtils.energyBarColor(itemStack);
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemBarUtils.energyBarWidth(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);

        TooltipUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, Entity player) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return this.energyUsage.getAsInt();
    }

    @Override
    public int getDefaultEnergyCapacity() {
        return this.defaultEnergyCapacity.getAsInt();
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier.get();
    }
}
