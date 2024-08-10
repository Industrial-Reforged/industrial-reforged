package com.indref.industrial_reforged.registries.items.storage;

import com.indref.industrial_reforged.registries.IRDataComponents;
import com.indref.industrial_reforged.api.items.bundles.AdvancedBundleContents;
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

// TODO: Make it dyeable
public class ToolboxItem extends BundleItem {
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);
    private static final int TOOLTIP_MAX_WEIGHT = 64;

    public ToolboxItem(Properties pProperties) {
        super(pProperties.component(IRDataComponents.ADVANCED_BUNDLE_CONTENTS, AdvancedBundleContents.EMPTY));
    }

    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        if (pStack.getCount() == 1 && pAction == ClickAction.SECONDARY) {
            AdvancedBundleContents bundleContents = pStack.get(IRDataComponents.ADVANCED_BUNDLE_CONTENTS);
            if (bundleContents == null) {
                return false;
            } else {
                ItemStack itemstack = pSlot.getItem();
                AdvancedBundleContents.Mutable bundlecontents$mutable = new AdvancedBundleContents.Mutable(bundleContents);
                if (itemstack.isEmpty()) {
                    this.playRemoveOneSound(pPlayer);
                    ItemStack itemStack = bundlecontents$mutable.removeOne();
                    if (itemStack != null) {
                        ItemStack itemStack1 = pSlot.safeInsert(itemStack);
                        bundlecontents$mutable.tryInsert(itemStack1);
                    }
                } else if (itemstack.getItem().canFitInsideContainerItems()) {
                    int i = bundlecontents$mutable.tryTransfer(pSlot, pPlayer);
                    if (i > 0) {
                        this.playInsertSound(pPlayer);
                    }
                }

                pStack.set(IRDataComponents.ADVANCED_BUNDLE_CONTENTS, bundlecontents$mutable.toImmutable());
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack pStack, @NotNull ItemStack pOther, @NotNull Slot pSlot, @NotNull ClickAction pAction, @NotNull Player pPlayer, @NotNull SlotAccess pAccess) {
        if (pStack.getCount() != 1) {
            return false;
        } else if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            AdvancedBundleContents bundleContents = pStack.get(IRDataComponents.ADVANCED_BUNDLE_CONTENTS);
            if (bundleContents == null) {
                return false;
            } else {
                AdvancedBundleContents.Mutable bundlecontents$mutable = new AdvancedBundleContents.Mutable(bundleContents);
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

                pStack.set(IRDataComponents.ADVANCED_BUNDLE_CONTENTS, bundlecontents$mutable.toImmutable());
                return true;
            }
        } else {
            return false;
        }
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        if (dropContents(itemstack, pPlayer)) {
            this.playDropContentsSound(pPlayer);
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public boolean isBarVisible(ItemStack pStack) {
        AdvancedBundleContents bundleContents = pStack.getOrDefault(IRDataComponents.ADVANCED_BUNDLE_CONTENTS, AdvancedBundleContents.EMPTY);
        return bundleContents.weight().compareTo(Fraction.ZERO) > 0;
    }

    public int getBarWidth(ItemStack pStack) {
        AdvancedBundleContents bundleContents = pStack.getOrDefault(IRDataComponents.ADVANCED_BUNDLE_CONTENTS, AdvancedBundleContents.EMPTY);
        return Math.min(1 + Mth.mulAndTruncate(bundleContents.weight(), 12), 13);
    }

    public int getBarColor(@NotNull ItemStack pStack) {
        return BAR_COLOR;
    }

    private static boolean dropContents(ItemStack pStack, Player pPlayer) {
        AdvancedBundleContents bundleContents = pStack.get(IRDataComponents.ADVANCED_BUNDLE_CONTENTS);
        if (bundleContents != null && !bundleContents.isEmpty()) {
            pStack.set(IRDataComponents.ADVANCED_BUNDLE_CONTENTS, AdvancedBundleContents.EMPTY);
            if (pPlayer instanceof ServerPlayer) {
                bundleContents.itemsCopy().forEach((p_330078_) -> pPlayer.drop(p_330078_, true));
            }

            return true;
        } else {
            return false;
        }
    }

    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        return !pStack.has(DataComponents.HIDE_TOOLTIP) && !pStack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP) ? Optional.ofNullable(pStack.get(IRDataComponents.ADVANCED_BUNDLE_CONTENTS)).map(contents -> new BundleTooltip(contents.asRegularContents())) : Optional.empty();
    }

    public void appendHoverText(ItemStack p_150749_, Item.@NotNull TooltipContext p_339687_, @NotNull List<Component> p_150751_, @NotNull TooltipFlag p_150752_) {
        BundleContents bundlecontents = p_150749_.get(DataComponents.BUNDLE_CONTENTS);
        if (bundlecontents != null) {
            int i = Mth.mulAndTruncate(bundlecontents.weight(), 64);
            p_150751_.add(Component.translatable("item.minecraft.bundle.fullness", i, 64).withStyle(ChatFormatting.GRAY));
        }

    }

    public void onDestroyed(ItemEntity pItemEntity) {
        BundleContents bundlecontents = pItemEntity.getItem().get(DataComponents.BUNDLE_CONTENTS);
        if (bundlecontents != null) {
            pItemEntity.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
            ItemUtils.onContainerDestroyed(pItemEntity, bundlecontents.itemsCopy());
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