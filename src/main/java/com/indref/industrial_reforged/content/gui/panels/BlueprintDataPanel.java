package com.indref.industrial_reforged.registries.gui.panels;

import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.neoforged.neoforge.client.gui.widget.ScrollPanel;
import org.jetbrains.annotations.NotNull;

public class BlueprintDataPanel extends ScrollPanel {
    public BlueprintDataPanel(int width, int height, int top, int left) {
        super(Minecraft.getInstance(), width, height, top, left);
    }

    @Override
    protected int getContentHeight() {
        return 0;
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {

    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }
}
