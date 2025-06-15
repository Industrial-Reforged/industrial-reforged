package com.indref.industrial_reforged.api.items.reactor;

import com.indref.industrial_reforged.api.items.MultiBarItem;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class FuelRodItem extends Item implements MultiBarItem {
    public FuelRodItem(Properties properties) {
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
