package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.items.tools.electric.ElectricToolItem;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.TooltipUtils;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ElectricHoeItem extends HoeItem implements IEnergyItem, ElectricToolItem {
    private final Supplier<EnergyTier> energyTier;
    private final IntSupplier energyUsage;
    private final IntSupplier defaultEnergyCapacity;

    public ElectricHoeItem(Properties properties, Tier tier, int baseAttackDamage, float baseAttackSpeed, Supplier<EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
        super(tier, properties.stacksTo(1)
                .attributes(HoeItem.createAttributes(tier, baseAttackDamage, baseAttackSpeed))
                .component(IRDataComponents.ENERGY, new ComponentEuStorage(defaultEnergyCapacity.getAsInt())));
        this.energyTier = energyTier;
        this.energyUsage = energyUsage;
        this.defaultEnergyCapacity = defaultEnergyCapacity;
    }

    public int getEnergyUsage() {
        return energyUsage.getAsInt();
    }

    @Override
    public int getDefaultEnergyCapacity() {
        return defaultEnergyCapacity.getAsInt();
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemBarUtils.energyBarWidth(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemBarUtils.energyBarColor(itemStack);
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

        if (pair == null) {
            return InteractionResult.PASS;
        } else {
            Player player = context.getPlayer();
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(context) && canWork(itemInHand, player)) {
                level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide()) {
                    consumer.accept(context);
                    consumeEnergy(itemInHand, player);
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public Supplier<EnergyTier> getEnergyTier() {
        return energyTier;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag p41424) {
        super.appendHoverText(stack, ctx, tooltip, p41424);

        TooltipUtils.addEnergyTooltip(tooltip, stack);
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return getEnergyUsage();
    }
}
