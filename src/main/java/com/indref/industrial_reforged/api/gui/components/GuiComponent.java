package com.indref.industrial_reforged.api.gui.components;

import com.indref.industrial_reforged.api.gui.IRAbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import javax.annotation.Nullable;

public abstract class GuiComponent {
    public final @NotNull Vector2i position;
    protected @Nullable IRAbstractContainerScreen<?> screen;

    public GuiComponent(@NotNull Vector2i position) {
        this.position = position;
    }

    public void initScreen(@NotNull IRAbstractContainerScreen<?> screen) {
        this.screen = screen;
    }

    public final boolean isShiftKeyDown() {
        return Screen.hasShiftDown();
    }

    public abstract int textureWidth();

    public abstract int textureHeight();

    public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta);

    public void renderInBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
    }
}
