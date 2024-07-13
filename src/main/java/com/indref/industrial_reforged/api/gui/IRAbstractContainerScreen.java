package com.indref.industrial_reforged.api.gui;

import com.indref.industrial_reforged.api.gui.components.GuiComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class IRAbstractContainerScreen<T extends IRAbstractContainerMenu<?>> extends AbstractContainerScreen<T> {
    private GuiComponent[] components;

    public IRAbstractContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    public final void initComponents(GuiComponent ...components) {
        this.components = components;
        for (GuiComponent component : this.components) {
            component.init(this);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics, pMouseX, pMouseX, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        for (GuiComponent component : components) {
            component.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        guiGraphics.blit(getBackgroundTexture(), leftPos, topPos, 0, 0, imageWidth, imageHeight);

        if (components == null) return;

        for (GuiComponent component : components) {
            component.renderInBackground(guiGraphics, mouseX, mouseY, delta);
        }
    }

    public abstract @NotNull ResourceLocation getBackgroundTexture();

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }
}
