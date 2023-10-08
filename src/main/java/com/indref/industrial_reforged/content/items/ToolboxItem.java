package com.indref.industrial_reforged.content.items;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.indref.industrial_reforged.api.items.ToolItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

// TODO: 10/8/2023 Fix bar fullnes bug

public class ToolboxItem extends BundleItem {

    public ToolboxItem(Item.Properties p_150726_) {
        super(p_150726_);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack toolbox, Slot slot, ClickAction clickAction, Player player) {
        if (toolbox.getCount() != 1 || clickAction != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemstack = slot.getItem();
            if (itemstack.isEmpty()) {
                this.playRemoveOneSound(player);
                removeOne(toolbox).ifPresent((p_150740_) -> add(toolbox, slot.safeInsert(p_150740_)));
            } else if (itemstack.getItem().canFitInsideContainerItems() && itemstack.getItem() instanceof ToolItem) {
                int i = (64 - getContentWeight(toolbox)) / getWeight();
                int j = add(toolbox, slot.safeTake(itemstack.getCount(), i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack toolbox, ItemStack itemStack, Slot p_150744_, ClickAction p_150745_, Player p_150746_, SlotAccess p_150747_) {
        if (toolbox.getCount() != 1) return false;
        if (p_150745_ == ClickAction.SECONDARY && p_150744_.allowModification(p_150746_)) {
            if (itemStack.isEmpty()) {
                removeOne(toolbox).ifPresent((p_186347_) -> {
                    this.playRemoveOneSound(p_150746_);
                    p_150747_.set(p_186347_);
                });
            } else if (itemStack.getItem() instanceof ToolItem) {
                int i = add(toolbox, itemStack);
                if (i > 0) {
                    this.playInsertSound(p_150746_);
                    itemStack.shrink(i);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private static int add(ItemStack boundleItemStack, ItemStack newItemStack) {
        if (!newItemStack.isEmpty() && newItemStack.getItem().canFitInsideContainerItems() && newItemStack.getItem() instanceof ToolItem) {
            CompoundTag compoundtag = boundleItemStack.getOrCreateTag();
            if (!compoundtag.contains("Items")) {
                compoundtag.put("Items", new ListTag());
            }

            int i = getContentWeight(boundleItemStack);
            int j = getWeight();
            int k = Math.min(newItemStack.getCount(), (64 - i) / j);
            if (k == 0) {
                return 0;
            } else {
                ListTag listtag = compoundtag.getList("Items", 10);
                Optional<CompoundTag> optional = getMatchingItem(newItemStack, listtag);
                if (optional.isPresent()) {
                    CompoundTag compoundtag1 = optional.get();
                    ItemStack itemstack = ItemStack.of(compoundtag1);
                    itemstack.grow(k);
                    itemstack.save(compoundtag1);
                    listtag.remove(compoundtag1);
                    listtag.add(0, (Tag) compoundtag1);
                } else {
                    ItemStack itemstack1 = newItemStack.copyWithCount(k);
                    CompoundTag compoundtag2 = new CompoundTag();
                    itemstack1.save(compoundtag2);
                    listtag.add(0, (Tag) compoundtag2);
                }

                return k;
            }
        } else {
            return 0;
        }
    }

    private static Optional<CompoundTag> getMatchingItem(ItemStack p_150757_, ListTag p_150758_) {
        return p_150757_.is(Items.BUNDLE) ? Optional.empty() : p_150758_.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((p_186350_) -> {
            return ItemStack.isSameItemSameTags(ItemStack.of(p_186350_), p_150757_);
        }).findFirst();
    }

    private static int getWeight() {
        // Always returns 4 because all tools have weight
        return 4;
    }

    private static int getContentWeight(ItemStack p_150779_) {
        return getContents(p_150779_).mapToInt((p_186356_) -> {
            return getWeight() * p_186356_.getCount();
        }).sum();
    }

    private static Optional<ItemStack> removeOne(ItemStack p_150781_) {
        CompoundTag compoundtag = p_150781_.getOrCreateTag();
        if (!compoundtag.contains("Items")) {
            return Optional.empty();
        } else {
            ListTag listtag = compoundtag.getList("Items", 10);
            if (listtag.isEmpty()) {
                return Optional.empty();
            } else {
                int i = 0;
                CompoundTag compoundtag1 = listtag.getCompound(0);
                ItemStack itemstack = ItemStack.of(compoundtag1);
                listtag.remove(0);
                if (listtag.isEmpty()) {
                    p_150781_.removeTagKey("Items");
                }

                return Optional.of(itemstack);
            }
        }
    }

    private static boolean dropContents(ItemStack p_150730_, Player p_150731_) {
        CompoundTag compoundtag = p_150730_.getOrCreateTag();
        if (!compoundtag.contains("Items")) {
            return false;
        } else {
            if (p_150731_ instanceof ServerPlayer) {
                ListTag listtag = compoundtag.getList("Items", 10);

                for (int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    ItemStack itemstack = ItemStack.of(compoundtag1);
                    p_150731_.drop(itemstack, true);
                }
            }

            p_150730_.removeTagKey("Items");
            return true;
        }
    }

    private static Stream<ItemStack> getContents(ItemStack p_150783_) {
        CompoundTag compoundtag = p_150783_.getTag();
        if (compoundtag == null) {
            return Stream.empty();
        } else {
            ListTag listtag = compoundtag.getList("Items", 10);
            return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack p_150775_) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        getContents(p_150775_).forEach(nonnulllist::add);
        return Optional.of(new BundleTooltip(nonnulllist, getContentWeight(p_150775_)));
    }

    public void appendHoverText(ItemStack p_150749_, Level p_150750_, List<Component> p_150751_, TooltipFlag p_150752_) {
        p_150751_.add(Component.translatable("item.minecraft.bundle.fullness", getContentWeight(p_150749_), 64).withStyle(ChatFormatting.GRAY));
    }

    public void onDestroyed(ItemEntity p_150728_) {
        ItemUtils.onContainerDestroyed(p_150728_, getContents(p_150728_.getItem()));
    }

    private void playRemoveOneSound(Entity p_186343_) {
        p_186343_.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + p_186343_.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity p_186352_) {
        p_186352_.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + p_186352_.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity p_186354_) {
        p_186354_.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + p_186354_.level().getRandom().nextFloat() * 0.4F);
    }
}
