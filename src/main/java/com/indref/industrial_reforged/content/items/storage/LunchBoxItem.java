package com.indref.industrial_reforged.content.items.storage;

import com.indref.industrial_reforged.api.items.IToolItem;
import com.indref.industrial_reforged.content.IRItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LunchBoxItem extends BundleItem {
    public static final int SLOT_CAPACITY = 128;

    public LunchBoxItem(Properties properties) {
        super(properties);
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
            } else if (itemstack.getItem().canFitInsideContainerItems() && itemstack.getTags().collect(Collectors.toList()).contains(ItemTags.FOX_FOOD )) {
                int i = (SLOT_CAPACITY - getContentWeight(toolbox));
                int j = add(toolbox, slot.safeTake(itemstack.getCount(), i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return Math.min(1 + 12 * getContentWeight(itemStack) / SLOT_CAPACITY, 13);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack toolbox, ItemStack itemStack, Slot p_150744_, ClickAction p_150745_, Player p_150746_, SlotAccess p_150747_) {
        if (toolbox.getCount() != 1) return false;
        if (p_150745_ == ClickAction.SECONDARY && p_150744_.allowModification(p_150746_)) {
            if (itemStack.isEmpty()) {
                removeOne(toolbox).ifPresent((p_186347_) -> {
                    this.playRemoveOneSound(p_150746_);
                    p_150747_.set(p_186347_);
                });
            } else if (itemStack.getTags().collect(Collectors.toList()).contains(ItemTags.FOX_FOOD )) {
                int i = add(toolbox, itemStack);
                if (i > 0) {
                    this.playInsertSound(p_150746_);
                    itemStack.shrink(i);
                }
            }

            return true;
        }
        return false;
    }

    private static int add(ItemStack toolboxItemStack, ItemStack newItemStack) {
        if (!newItemStack.isEmpty() && (newItemStack.getItem().canFitInsideContainerItems() && newItemStack.getTags().collect(Collectors.toList()).contains(ItemTags.FOX_FOOD )  )) {
            CompoundTag compoundtag = toolboxItemStack.getOrCreateTag();
            if (!compoundtag.contains("Items")) {
                compoundtag.put("Items", new ListTag());
            }

            int i = getContentWeight(toolboxItemStack);
            int k = Math.min(newItemStack.getCount(), SLOT_CAPACITY - i);
            if (k == 0) {
                return 0;
            } else {
                ListTag listtag = compoundtag.getList("Items", 10);
                Optional<CompoundTag> optional = getMatchingItem(newItemStack, listtag);
                ItemStack itemstack1 = newItemStack.copyWithCount(k);
                CompoundTag compoundtag2 = new CompoundTag();
                itemstack1.save(compoundtag2);
                listtag.add(0, (Tag) compoundtag2);
                return k;
            }
        }
        return 0;
    }

    private static Optional<CompoundTag> getMatchingItem(ItemStack p_150757_, ListTag p_150758_) {
        return p_150757_.is(IRItems.TOOLBOX.get()) ? Optional.empty() : p_150758_.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((p_186350_) -> {
            return ItemStack.isSameItemSameTags(ItemStack.of(p_186350_), p_150757_);
        }).findFirst();
    }

    private static int getContentWeight(ItemStack itemStack) {
        return getContents(itemStack).mapToInt(ItemStack::getCount).sum();
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

    private static Stream<ItemStack> getContents(ItemStack itemStack) {
        CompoundTag compoundtag = itemStack.getTag();
        if (compoundtag == null) {
            return Stream.empty();
        } else {
            ListTag listtag = compoundtag.getList("Items", 10);
            return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        getContents(itemStack).forEach(nonnulllist::add);
        return Optional.of(new BundleTooltip(nonnulllist, getContentWeight(itemStack)));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(Component.translatable("item.minecraft.bundle.fullness", getContentWeight(itemStack), SLOT_CAPACITY).withStyle(ChatFormatting.GRAY));
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
}
