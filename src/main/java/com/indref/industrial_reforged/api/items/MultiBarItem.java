package com.indref.industrial_reforged.api.items;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link net.minecraft.world.item.Item#isBarVisible(ItemStack stack)} needs to be set to true in order to render the bars
 */
public interface MultiBarItem {
    /**
     * @param itemStack the fluidIngredient this gets rendered to
     * @return First pair entry: Color, second: Width
     */
    @NotNull
    List<IntIntPair> getBarColorsAndWidths(ItemStack itemStack);
}
