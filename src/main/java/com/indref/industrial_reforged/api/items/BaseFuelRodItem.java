package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.util.ItemUtils;
import com.mojang.datafixers.util.Pair;
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
                IntIntPair.of(ItemUtils.HEAT_BAR_COLOR, 10),
                IntIntPair.of(ItemUtils.ENERGY_BAR_COLOR, 5)
        );
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }
}
