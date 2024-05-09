package com.indref.industrial_reforged.api.gui;

import com.indref.industrial_reforged.api.gui.components.GuiComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class IRAbstractContainerScreen<T extends IRAbstractContainerMenu<?>> extends AbstractContainerScreen<T> {
    private GuiComponent[] components;

    public IRAbstractContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    public final void initComponents(GuiComponent ...components) {
        this.components = components;
        for (GuiComponent component : this.components) {
            component.initScreen(this);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        for (GuiComponent component : components) {
            component.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int mouseX, int mouseY) {
    }
}
