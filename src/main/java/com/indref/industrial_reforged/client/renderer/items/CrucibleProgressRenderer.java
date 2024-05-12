package com.indref.industrial_reforged.client.renderer.items;

import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.client.IItemDecorator;

public class CrucibleProgressRenderer implements IItemDecorator {
    public static final String BARWIDTH_KEY = "barwidth";
    public static final String IS_MELTING_KEY = "cruciblemelting";

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
        if (ItemUtils.getTag(itemStack).getBoolean(IS_MELTING_KEY)) {
            int barWidth = ItemUtils.getTag(itemStack).getInt(BARWIDTH_KEY);
            int width = 1;
            int i = ItemUtils.HEAT_BAR_COLOR;
            int j = xOffset + 2;
            int k = yOffset + 13;
            guiGraphics.fill(RenderType.guiOverlay(), j, k + 2, j + 2, k - 10, -16777216);
            guiGraphics.fill(RenderType.guiOverlay(), j, k + 1, j + width, k - barWidth, i | 0xFF000000);
        }
        return false;
    }
}