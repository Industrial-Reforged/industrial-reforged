package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.IndustrialReforged;

import com.indref.industrial_reforged.api.gui.IRAbstractContainerScreen;
import com.indref.industrial_reforged.api.gui.components.EnergyGuiComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class CentrifugeScreen extends IRAbstractContainerScreen<CentrifugeMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(IndustrialReforged.MODID, "textures/gui/centrifuge.png");

    public CentrifugeScreen(CentrifugeMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        initComponents(
                new EnergyGuiComponent(new Vector2i(this.leftPos + 10, this.topPos + 8))
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
