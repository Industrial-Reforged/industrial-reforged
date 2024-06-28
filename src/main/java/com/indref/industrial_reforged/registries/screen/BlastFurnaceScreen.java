package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.FluidTankRenderer;
import com.indref.industrial_reforged.util.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BlastFurnaceScreen extends AbstractContainerScreen<BlastFurnaceMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/blast_furnace.png");

    private FluidTankRenderer renderer;

    public BlastFurnaceScreen(BlastFurnaceMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        Optional<FluidTank> fluidTank = menu.blockEntity.getFluidTank();
        fluidTank.ifPresent(tank -> renderer = new FluidTankRenderer(tank.getCapacity(), true, 52, 52));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        Optional<FluidTank> fluidTank = menu.blockEntity.getFluidTank();
        fluidTank.ifPresent(tank -> renderer.render(guiGraphics.pose(), x + 98, y + 18, tank.getFluid()));
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidAreaTooltips(pGuiGraphics, pMouseX, pMouseY, x + 98 - renderer.getWidth() - 4, y+3);
    }

    private void renderFluidAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 55, 15)) {
            Optional<FluidTank> fluidTank = menu.blockEntity.getFluidTank();
            fluidTank.ifPresent(tank -> guiGraphics.renderTooltip(Minecraft.getInstance().font, renderer.getTooltip(tank.getFluid()),
                    Optional.empty(), pMouseX - x + 45, pMouseY - y));
        }
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
    }
}
