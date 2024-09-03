package com.indref.industrial_reforged.api.items.bundles;

import com.google.common.collect.Lists;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.tags.IRTags;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.neoforge.common.ItemAbilities;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public record AdvancedBundleContents(List<ItemStack> items, Fraction weight) implements TooltipComponent {
    public static final AdvancedBundleContents EMPTY = new AdvancedBundleContents(List.of());
    public static final Codec<AdvancedBundleContents> CODEC = ItemStack.CODEC.listOf().xmap(AdvancedBundleContents::new, AdvancedBundleContents::items);
    public static final StreamCodec<RegistryFriendlyByteBuf, AdvancedBundleContents> STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(AdvancedBundleContents::new, AdvancedBundleContents::items);
    private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);

    public AdvancedBundleContents(List<ItemStack> itemStacks) {
        this(itemStacks, computeContentWeight(itemStacks));
    }

    private static Fraction computeContentWeight(List<ItemStack> p_331148_) {
        Fraction fraction = Fraction.ZERO;

        ItemStack itemstack;
        for (Iterator<ItemStack> itemStackIterator = p_331148_.iterator(); itemStackIterator.hasNext(); fraction = fraction.add(getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)))) {
            itemstack = itemStackIterator.next();
        }

        return fraction;
    }

    private static Fraction getWeight(ItemStack p_332084_) {
        AdvancedBundleContents bundlecontents = p_332084_.get(IRDataComponents.ADVANCED_BUNDLE_CONTENTS);
        if (bundlecontents != null) {
            return BUNDLE_IN_BUNDLE_WEIGHT.add(bundlecontents.weight());
        } else {
            return Fraction.getFraction(1, 8);
        }
    }

    public ItemStack getItemUnsafe(int p_330802_) {
        return this.items.get(p_330802_);
    }

    public Stream<ItemStack> itemCopyStream() {
        return this.items.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> itemsAsIter() {
        return this.items;
    }

    public Iterable<ItemStack> itemsCopy() {
        return Lists.transform(this.items, ItemStack::copy);
    }

    public int size() {
        return this.items.size();
    }

    public Fraction weight() {
        return this.weight;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public BundleContents asRegularContents() {
        return new BundleContents(items);
    }

    public String toString() {
        return "AdvancedBundleContents" + this.items;
    }

    public static class Mutable {
        private final List<ItemStack> items;
        private Fraction weight;

        public Mutable(AdvancedBundleContents p_332039_) {
            this.items = new ArrayList<>(p_332039_.items);
            this.weight = p_332039_.weight;
        }

        public AdvancedBundleContents.Mutable clearItems() {
            this.items.clear();
            this.weight = Fraction.ZERO;
            return this;
        }

        private int findStackIndex(ItemStack p_331941_) {
            if (p_331941_.isStackable()) {
                for (int i = 0; i < this.items.size(); ++i) {
                    if (ItemStack.isSameItemSameComponents((ItemStack) this.items.get(i), p_331941_)) {
                        return i;
                    }
                }

            }
            return -1;
        }

        private int getMaxAmountToAdd(ItemStack p_330527_) {
            Fraction fraction = Fraction.ONE.subtract(this.weight);
            return Math.max(fraction.divideBy(AdvancedBundleContents.getWeight(p_330527_)).intValue(), 0);
        }

        public int tryInsert(ItemStack itemStack) {
            if (!itemStack.isEmpty() && itemStack.getItem().canFitInsideContainerItems() && isValid(itemStack)) {
                int i = Math.min(itemStack.getCount(), this.getMaxAmountToAdd(itemStack));
                if (i == 0) {
                    return 0;
                } else {
                    this.weight = this.weight.add(AdvancedBundleContents.getWeight(itemStack).multiplyBy(Fraction.getFraction(i, 1)));
                    int j = this.findStackIndex(itemStack);
                    if (j != -1) {
                        ItemStack itemstack = this.items.remove(j);
                        ItemStack itemstack1 = itemstack.copyWithCount(itemstack.getCount() + i);
                        itemStack.shrink(i);
                        this.items.addFirst(itemstack1);
                    } else {
                        this.items.addFirst(itemStack.split(i));
                    }

                    return i;
                }
            } else {
                return 0;
            }
        }

        public boolean isValid(ItemStack itemStack) {
            return (itemStack.getTags().anyMatch(tag -> tag == IRTags.Items.TOOL)
                    || itemStack.canPerformAction(ItemAbilities.PICKAXE_DIG)
                    || itemStack.canPerformAction(ItemAbilities.AXE_DIG)
                    || itemStack.canPerformAction(ItemAbilities.SHOVEL_DIG));
        }

        public int tryTransfer(Slot p_330834_, Player p_331924_) {
            ItemStack itemstack = p_330834_.getItem();
            int i = this.getMaxAmountToAdd(itemstack);
            return this.tryInsert(p_330834_.safeTake(itemstack.getCount(), i, p_331924_));
        }

        @Nullable
        public ItemStack removeOne() {
            if (this.items.isEmpty()) {
                return null;
            } else {
                ItemStack itemstack = this.items.removeFirst().copy();
                this.weight = this.weight.subtract(AdvancedBundleContents.getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)));
                return itemstack;
            }
        }

        public Fraction weight() {
            return this.weight;
        }

        public AdvancedBundleContents toImmutable() {
            return new AdvancedBundleContents(List.copyOf(this.items), this.weight);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdvancedBundleContents that)) return false;
        return Objects.equals(weight, that.weight) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, weight);
    }
}
