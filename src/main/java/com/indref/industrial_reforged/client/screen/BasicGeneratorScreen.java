package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.energy.IREnergyStorageWrapper;
import com.indref.industrial_reforged.api.client.screen.MachineScreen;
import com.indref.industrial_reforged.client.widgets.EnergyBarWidget;
import com.indref.industrial_reforged.client.widgets.RedstonePanelWidget;
import com.indref.industrial_reforged.content.menus.BasicGeneratorMenu;
import com.indref.industrial_reforged.client.widgets.BatterySlotWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BasicGeneratorScreen extends MachineScreen<BasicGeneratorMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/basic_generator.png");
    private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.parse("container/smoker/lit_progress");

    public BasicGeneratorScreen(BasicGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    protected void init() {
        super.init();

        this.imageHeight = 186;
        this.inventoryLabelY = this.imageHeight - 94;
        super.init();
        EnergyBarWidget energyBarWidget = addRenderableOnly(
                new EnergyBarWidget(this.leftPos + 11, this.topPos + 17, new IREnergyStorageWrapper(menu.blockEntity.getEuStorage()), true)
        );
        addPanelWidget(new RedstonePanelWidget(this.leftPos + this.imageWidth, this.topPos + 2));
        addRenderableOnly(new BatterySlotWidget(this.leftPos + 8, this.topPos + 14 + energyBarWidget.getHeight() + 5));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, delta, mouseX, mouseY);
        renderLitProgress(guiGraphics);
    }

    protected void renderLitProgress(GuiGraphics pGuiGraphics) {
        int i = this.leftPos;
        int j = this.topPos;
        if (this.menu.getBlockEntity().isActive()) {
            float burnTime = ((float) this.menu.getBlockEntity().getBurnTime() / this.menu.getBlockEntity().getMaxBurnTime());
            int j1 = Mth.ceil(burnTime * 13F);
            pGuiGraphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - j1, i + 80, j + 37 + 14 - j1, 14, j1);
        }
    }
}
