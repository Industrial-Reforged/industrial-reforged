package com.indref.industrial_reforged.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class GuiUtils {
    public static void drawImg(GuiGraphics guiGraphics, ResourceLocation texturePath, int x, int y, int width, int height) {
        guiGraphics.blit(texturePath, x, y, 0, 0, 0, width, height, width, height);
    }
}
