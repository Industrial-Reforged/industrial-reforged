package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FireBoxScreen extends AbstractContainerScreen<FireBoxMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(IndustrialReforged.MODID, "textures/gui/firebox.png");

    public FireBoxScreen(FireBoxMenu fireBoxMenu, Inventory inventory, Component component) {
        super(fireBoxMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
