package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.util.ItemUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ElectricHoeItem extends HoeItem implements IEnergyItem {
    public ElectricHoeItem(Tier p_41336_, int p_41337_, float p_41338_, Properties p_41339_) {
        super(p_41336_, p_41337_, p_41338_, p_41339_.stacksTo(1));
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
    public int getUseDuration(ItemStack p_41454_) {
        return 1;
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41341_) {
        Level level = p_41341_.getLevel();
        BlockPos blockpos = p_41341_.getClickedPos();
        BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(p_41341_, net.neoforged.neoforge.common.ToolActions.HOE_TILL, false);
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
        if (pair == null) {
            return InteractionResult.PASS;
        } else {
            Player player = p_41341_.getPlayer();
            ItemStack itemStack = player.getItemInHand(p_41341_.getHand());
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(p_41341_) && getEnergyStored(itemStack) >= 3) {
                level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide) {
                    consumer.accept(p_41341_);
                    setEnergyStored(itemStack, getEnergyStored(itemStack) - 3);
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level p41422, List<Component> tooltip, TooltipFlag p41424) {
        super.appendHoverText(stack, p41422, tooltip, p41424);
        IEnergyItem item = this;
        tooltip.add(Component.literal(String.format("%s / %s", item.getEnergyStored(stack), item.getEnergyCapacity())).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public int getEnergyCapacity() {
        return 10000;
    }
}
