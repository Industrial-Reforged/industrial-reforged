package com.indref.industrial_reforged.api.items.bundles;

import com.indref.industrial_reforged.api.items.container.IItemItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.apache.commons.lang3.math.Fraction;

import java.util.Iterator;
import java.util.List;

public class AdvancedBundleUtils {
    private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);

    protected static Fraction computeContentWeight(List<ItemStack> p_331148_) {
        Fraction fraction = Fraction.ZERO;

        ItemStack itemstack;
        for (Iterator<ItemStack> var2 = p_331148_.iterator(); var2.hasNext(); fraction = fraction.add(getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)))) {
            itemstack = var2.next();
        }

        return fraction;
    }

    protected static Fraction getWeight(ItemStack itemStack) {
        List<ItemStack> itemStacks = ((IItemItem) itemStack.getItem()).getItems(itemStack);
        if (!itemStack.isEmpty()) {
            return BUNDLE_IN_BUNDLE_WEIGHT.add(computeContentWeight(itemStacks));
        } else {
            List<BeehiveBlockEntity.Occupant> list = itemStack.getOrDefault(DataComponents.BEES, List.of());
            return !list.isEmpty() ? Fraction.ONE : Fraction.getFraction(1, itemStack.getMaxStackSize());
        }
    }
}
