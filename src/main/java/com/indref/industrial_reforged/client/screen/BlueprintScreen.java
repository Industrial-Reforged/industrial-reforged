package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.gui.menus.BlueprintMenu;
import com.indref.industrial_reforged.content.gui.panels.BlueprintDataPanel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BlueprintScreen extends AbstractContainerScreen<BlueprintMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/chunk_analyzer.png");

    private BlueprintDataPanel dataPanel;

    public BlueprintScreen(BlueprintMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 4;
        this.titleLabelY = 20;
        this.inventoryLabelY = -500;
        this.imageWidth = 190;
        this.imageHeight = 136;

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        int width = (x + imageWidth - 4) - (x + 4);
        int height = (y + imageHeight - 6) - (y + 16);

        this.dataPanel = new BlueprintDataPanel(width, height, y + 16, x + 4);
        addRenderableWidget(this.dataPanel);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        guiGraphics.fill(x + 4, y + 16, x + imageWidth - 4, y + imageHeight - 6, FastColor.ARGB32.color(53, 53, 53));
    }
}
