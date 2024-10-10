package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.IRAbstractContainerScreen;
import com.indref.industrial_reforged.content.gui.components.HeatDisplayGuiComponent;
import com.indref.industrial_reforged.content.gui.menus.FireBoxMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class FireBoxScreen extends IRAbstractContainerScreen<FireBoxMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/firebox.png");
    private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.parse("container/smoker/lit_progress");

    public FireBoxScreen(FireBoxMenu fireBoxMenu, Inventory inventory, Component component) {
        super(fireBoxMenu, inventory, component);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        renderLitProgress(guiGraphics);
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        initComponents(
                new HeatDisplayGuiComponent(new Vector2i(x + imageWidth - 95, y + 5), true)
        );
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }

    protected void renderLitProgress(GuiGraphics pGuiGraphics) {
        int i = this.leftPos;
        int j = this.topPos;
        if (this.menu.getBlockEntity().isActive()) {
            float burnTime = ((float) this.menu.getBlockEntity().getBurnTime() / this.menu.getBlockEntity().getMaxBurnTime());
            int j1 = Mth.ceil(burnTime * 13F);
            pGuiGraphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - j1, i + 80, j + 20 + 14 - j1 - 1, 14, j1);
        }
    }
}
