package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.FluidTankRenderer;
import com.indref.industrial_reforged.util.MouseUtil;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CrucibleScreen extends AbstractContainerScreen<CrucibleMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(IndustrialReforged.MODID, "textures/gui/crucible.png");

    private FluidTankRenderer renderer;
    private CrucibleMenu menu;

    public CrucibleScreen(CrucibleMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
        this.menu = p_97741_;
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        renderer = new FluidTankRenderer(menu.blockEntity.getFluidTank().getCapacity(), true, 52, 52);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderer.render(guiGraphics.pose(), x + 98, y + 18, menu.blockEntity.getFluidTank().getFluid());
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidAreaTooltips(pGuiGraphics, pMouseX, pMouseY, x + 98 - renderer.getWidth() - 4, y+3);
    }

    private void renderFluidAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 55, 15)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, renderer.getTooltip(menu.blockEntity.getFluidTank().getFluid()),
                    Optional.empty(), pMouseX - x+45, pMouseY - y);
        }
    }

    private void renderHeatDisplay(GuiGraphics guiGraphics) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        int temperature = menu.blockEntity.getHeatStored(menu.blockEntity);

        guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("Temperature: "+temperature+"°C").withStyle(ChatFormatting.WHITE), x + imageWidth - 95, y + 5, 0);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderHeatDisplay(guiGraphics);
    }
}
