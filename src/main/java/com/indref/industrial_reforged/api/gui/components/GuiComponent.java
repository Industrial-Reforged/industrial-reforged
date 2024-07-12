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

    public final void init(@NotNull IRAbstractContainerScreen<?> screen) {
        this.screen = screen;
        onInit();
    }

    protected void onInit() {
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
