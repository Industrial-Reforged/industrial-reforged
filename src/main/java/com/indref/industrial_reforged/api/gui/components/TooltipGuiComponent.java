package com.indref.industrial_reforged.api.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public abstract class TooltipGuiComponent extends GuiComponent {
    public TooltipGuiComponent(@NotNull Vector2i position) {
        super(position);
    }

    public abstract List<Component> getTooltip();

    private boolean shouldRenderTooltip(int mouseX, int mouseY) {
        int width = textureWidth();
        int height = textureHeight();
        boolean matchesOnX = mouseX > this.position.x && mouseX < this.position.x + width;
        boolean matchesOnY = mouseY > this.position.y && mouseY < this.position.y + height;
        return matchesOnX && matchesOnY;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        boolean shouldRenderTooltip = shouldRenderTooltip(mouseX, mouseY);
        if (shouldRenderTooltip) {
            Font font = Minecraft.getInstance().font;
            guiGraphics.renderComponentTooltip(font, getTooltip(), mouseX, mouseY);
        }
    }
}
