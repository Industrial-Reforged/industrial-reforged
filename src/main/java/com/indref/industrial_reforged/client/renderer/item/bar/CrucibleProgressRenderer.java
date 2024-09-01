package com.indref.industrial_reforged.client.renderer.item.bar;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.util.ItemUtils;
import com.indref.industrial_reforged.util.renderer.GuiUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public class CrucibleProgressRenderer implements IItemDecorator {
    public static final String BARWIDTH_KEY = "barwidth";
    // 0 = not melting
    // 1 = melting
    // 2 = cannot melt
    public static final String IS_MELTING_KEY = "cruciblemelting";

    public static final ResourceLocation DISABLED_TEXTURE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/item/overlay/disabled.png");

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
        CompoundTag tag = ItemUtils.getImmutableTag(itemStack).copyTag();
        int meltingType = tag.getInt(IS_MELTING_KEY);
        if (meltingType == 1) {
            int barWidth = tag.getInt(BARWIDTH_KEY);
            int width = 1;
            int i = ItemUtils.HEAT_BAR_COLOR;
            int j = xOffset + 2;
            int k = yOffset + 13;
            guiGraphics.fill(RenderType.guiOverlay(), j, k + 2, j + 2, k - 10, -16777216);
            guiGraphics.fill(RenderType.guiOverlay(), j, k + 1, j + width, k - barWidth, i | 0xFF000000);
        } else if (meltingType == 2) {
            GuiUtils.drawWithZ(guiGraphics, DISABLED_TEXTURE, xOffset, yOffset, 200, 16, 16);
        }
        return true;
    }
}
