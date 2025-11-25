package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.widgets.ClearableFluidTankWidget;
import com.indref.industrial_reforged.client.widgets.HeatBarWidget;
import com.indref.industrial_reforged.content.menus.BlastFurnaceMenu;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.FluidTankWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BlastFurnaceScreen extends PDLAbstractContainerScreen<BlastFurnaceMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/blast_furnace.png");
    private static final ResourceLocation PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    public BlastFurnaceScreen(BlastFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        FluidTankWidget fluidTankWidget = new ClearableFluidTankWidget(this.leftPos + 97, this.topPos + 17, FluidTankWidget.TankVariants.LARGE, this.getMenu().blockEntity);
        HeatBarWidget heatDisplayWidget = new HeatBarWidget(menu.blockEntity.getHeatStorage(), this.leftPos + 5, this.topPos + 61);

        addRenderableWidget(fluidTankWidget);
        addRenderableWidget(heatDisplayWidget);
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, delta, mouseX, mouseY);
        float progress = (float) this.menu.blockEntity.getProgress() / this.menu.blockEntity.getMaxProgress();
        int scaledProgress = Mth.ceil(progress * 24.0F);
        guiGraphics.blitSprite(PROGRESS_SPRITE, 24, 16, 0, 0, this.leftPos + 67, this.topPos + 38, scaledProgress, 16);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
