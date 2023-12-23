package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.util.ItemUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseFuelRodItem extends Item implements IMultiBarItem {
    public BaseFuelRodItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull List<Pair<Integer, Integer>> getBarColorsAndWidths(ItemStack itemStack) {
        return List.of(
                Pair.of(ItemUtils.HEAT_BAR_COLOR, 10),
                Pair.of(ItemUtils.ENERGY_BAR_COLOR, 5)
        );
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }
}
