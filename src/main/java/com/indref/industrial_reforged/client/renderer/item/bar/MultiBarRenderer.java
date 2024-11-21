package com.indref.industrial_reforged.client.renderer.item.bar;

import com.indref.industrial_reforged.api.items.MultiBarItem;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiBarRenderer implements IItemDecorator {
    private final Item item;

    public MultiBarRenderer(Item item) {
        this.item = item;
    }

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        MultiBarItem multiBarItem = (MultiBarItem) item;
        if (item.isBarVisible(stack)) {
            @NotNull List<IntIntPair> barColorsAndWidths = multiBarItem.getBarColorsAndWidths(stack);
            for (int i1 = 0, barColorsAndWidthsSize = barColorsAndWidths.size(); i1 < barColorsAndWidthsSize; i1++) {
                IntIntPair colorAndWidth = barColorsAndWidths.get(i1);
                int l = colorAndWidth.rightInt();
                int i = colorAndWidth.leftInt();
                int j = xOffset + 2;
                int k = yOffset + 13 + i1;
                guiGraphics.fill(RenderType.guiOverlay(), j, k, j + 13, k + 2, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), j, k, j + l, k + 1, i | 0xFF000000);
            }
        }
        return false;
    }
}
