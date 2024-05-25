package com.indref.industrial_reforged.api.items.bundles;

import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.items.container.IItemItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class AdvancedBundleItem extends BundleItem implements IItemItem {
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);
    private static final int TOOLTIP_MAX_WEIGHT = 64;

    public AdvancedBundleItem(Item.Properties pProperties) {
        super(pProperties.component(IRDataComponents.ITEM, List.of()));
    }

    public static float getFullnessDisplay(ItemStack pStack) {
        BundleContents bundlecontents = (BundleContents) pStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return bundlecontents.weight().floatValue();
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        if (pStack.getCount() == 1 && pAction == ClickAction.SECONDARY) {
            List<ItemStack> bundlecontents = getItems(pStack);
            if (bundlecontents == null) {
                return false;
            } else {
                ItemStack itemstack = pSlot.getItem();
                BundleContents.Mutable bundlecontents$mutable = new BundleContents.Mutable(new BundleContents(bundlecontents));
                if (itemstack.isEmpty()) {
                    this.playRemoveOneSound(pPlayer);
                    ItemStack itemstack1 = bundlecontents$mutable.removeOne();
                    if (itemstack1 != null) {
                        ItemStack itemstack2 = pSlot.safeInsert(itemstack1);
                        bundlecontents$mutable.tryInsert(itemstack2);
                    }
                } else if (itemstack.getItem().canFitInsideContainerItems()) {
                    int i = bundlecontents$mutable.tryTransfer(pSlot, pPlayer);
                    if (i > 0) {
                        this.playInsertSound(pPlayer);
                    }
                }

                getItems(pStack).addAll(bundlecontents$mutable.toImmutable().itemCopyStream().toList());
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if (pStack.getCount() != 1) {
            return false;
        } else if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            List<ItemStack> bundleContents = getItems(pStack);
            if (bundleContents.isEmpty()) {
                return false;
            } else {
                BundleContents.Mutable bundlecontents$mutable = new BundleContents.Mutable(new BundleContents(bundleContents));
                if (pOther.isEmpty()) {
                    ItemStack itemstack = bundlecontents$mutable.removeOne();
                    if (itemstack != null) {
                        this.playRemoveOneSound(pPlayer);
                        pAccess.set(itemstack);
                    }
                } else {
                    int i = bundlecontents$mutable.tryInsert(pOther);
                    if (i > 0) {
                        this.playInsertSound(pPlayer);
                    }
                }

                getItems(pStack).addAll(bundlecontents$mutable.toImmutable().itemCopyStream().toList());
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        if (dropContents(itemstack, pPlayer)) {
            this.playDropContentsSound(pPlayer);
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return AdvancedBundleUtils.computeContentWeight(getItems(pStack)).compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.min(1 + Mth.mulAndTruncate(AdvancedBundleUtils.computeContentWeight(getItems(pStack)), 12), 13);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return BAR_COLOR;
    }

    private static boolean dropContents(ItemStack pStack, Player pPlayer) {
        IItemItem item = (IItemItem) pStack.getItem();
        List<ItemStack> bundleContents = item.getItems(pStack);
        if (!bundleContents.isEmpty()) {
            item.clearItems(pStack);
            if (pPlayer instanceof ServerPlayer) {
               com.indref.industrial_reforged.util.ItemUtils.copyItems(bundleContents).forEach((p_330078_) -> {
                    pPlayer.drop(p_330078_, true);
                });
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        return !pStack.has(DataComponents.HIDE_TOOLTIP) && !pStack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP) ? Optional.of(new BundleTooltip(new BundleContents(getItems(pStack)))) : Optional.empty();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack p_150749_, Item.TooltipContext p_339687_, List<Component> p_150751_, TooltipFlag p_150752_) {
        List<ItemStack> itemStacks = getItems(p_150749_);
        if (!itemStacks.isEmpty()) {
            int i = Mth.mulAndTruncate(AdvancedBundleUtils.computeContentWeight(itemStacks), getSlots(p_150749_));
            p_150751_.add(Component.translatable("item.minecraft.bundle.fullness", i, 64).withStyle(ChatFormatting.GRAY));
        }

    }

    @Override
    public void onDestroyed(@NotNull ItemEntity pItemEntity) {
        ItemStack itemStack = pItemEntity.getItem();
        if (!this.getItems(itemStack).isEmpty()) {
            this.clearItems(itemStack);
            ItemUtils.onContainerDestroyed(pItemEntity, com.indref.industrial_reforged.util.ItemUtils.copyItems(this.getItems(itemStack)));
        }

    }

    private void playRemoveOneSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }
}
