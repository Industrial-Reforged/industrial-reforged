package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.util.items.ItemBarUtils;
import com.indref.industrial_reforged.util.items.ItemUtils;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseFuelRodItem extends Item implements MultiBarItem {
    public BaseFuelRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull List<IntIntPair> getBarColorsAndWidths(ItemStack itemStack) {
        return List.of(
                IntIntPair.of(ItemBarUtils.heatBarColor(itemStack), ItemBarUtils.heatBarWidth(itemStack)),
                IntIntPair.of(ItemBarUtils.energyBarColor(itemStack), ItemBarUtils.energyBarWidth(itemStack))
        );
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }
}
