package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.IRAbstractContainerScreen;
import com.indref.industrial_reforged.content.gui.components.EnergyGuiComponent;
import com.indref.industrial_reforged.content.gui.menus.CentrifugeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class CentrifugeScreen extends IRAbstractContainerScreen<CentrifugeMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/centrifuge.png");
    private static final ResourceLocation PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "container/centrifuge/progress_arrows");

    public CentrifugeScreen(CentrifugeMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void init() {
        this.imageHeight = 185;
        this.inventoryLabelY = this.imageHeight - 94;
        super.init();
        initComponents(
                new EnergyGuiComponent(new Vector2i(this.leftPos + 10, this.topPos + 16), true, true)
        );
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, delta, mouseX, mouseY);
        float progress = ((float) this.menu.blockEntity.getProgress() / this.menu.blockEntity.getMaxProgress());
        int textureSize = 48;
        int xOffset = 64;
        int yOffset = 25;
        int arrowSize = (int) (9 * (1.05F - progress));
        int uSize = textureSize - arrowSize * 2;
        if (this.menu.blockEntity.getMaxProgress() > 0) {
            guiGraphics.blitSprite(
                    PROGRESS_SPRITE,
                    textureSize,
                    textureSize,
                    arrowSize,
                    arrowSize,
                    this.leftPos + xOffset + arrowSize,
                    this.topPos + yOffset + arrowSize,
                    uSize,
                    uSize
            );
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
