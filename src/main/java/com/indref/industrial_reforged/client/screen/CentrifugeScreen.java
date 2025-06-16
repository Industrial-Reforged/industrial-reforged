package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.energy.IREnergyStorageWrapper;
import com.indref.industrial_reforged.client.widgets.*;
import com.indref.industrial_reforged.content.gui.menus.CentrifugeMenu;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import com.portingdeadmods.portingdeadlibs.impl.client.screens.widgets.FluidTankWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CentrifugeScreen extends PDLAbstractContainerScreen<CentrifugeMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/centrifuge.png");
    private static final ResourceLocation PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "container/centrifuge/progress_arrows");

    private RedstoneWidget redstoneWidget;
    private UpgradeWidget upgradeWidget;

    public CentrifugeScreen(CentrifugeMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();

        this.imageHeight = 185;
        this.inventoryLabelY = this.imageHeight - 94;
        EnergyBarWidget energyBarWidget = addRenderableOnly(
                new EnergyBarWidget(this.leftPos + 10, this.topPos + 16, new IREnergyStorageWrapper(menu.blockEntity.getEuStorage()), true)
        );
        addRenderableWidget(
                new FluidTankWidget(this.leftPos + 142, this.topPos + 24, FluidTankWidget.TankVariants.SMALL, menu.blockEntity)
        );
        addRenderableOnly(new BatterySlotWidget(this.leftPos + 8, this.topPos + 14 + energyBarWidget.getHeight() + 4));

        MachineWidgetContext context = new MachineWidgetContext(this.menu, this::onWidgetResize);
        this.redstoneWidget = new RedstoneWidget(menu.blockEntity, this.leftPos + this.imageWidth, this.topPos + 2, 32, 32, context);
        this.redstoneWidget.visitWidgets(this::addRenderableWidget);

        this.upgradeWidget = new UpgradeWidget(menu.blockEntity, this.leftPos + this.imageWidth, this.topPos + 2 + 24 + 2, 32, 32, context);
        this.upgradeWidget.visitWidgets(this::addRenderableWidget);
    }

    private void onWidgetResize(PanelWidget widget) {
        PanelWidget otherWidget = widget instanceof RedstoneWidget ? this.upgradeWidget : this.redstoneWidget;
        int y = 0;
        if (widget.getY() < otherWidget.getY()) {
            y = (otherWidget instanceof RedstoneWidget ? RedstoneWidget.WIDGET_OPEN_HEIGHT : UpgradeWidget.WIDGET_OPEN_HEIGHT) / 2 * (widget.isOpen() ? 1 : -1);
        }
        if (widget.isOpen()) {
            otherWidget.setPosition(otherWidget.getX(), otherWidget.getY() + y);
            otherWidget.visitWidgets(this::addRenderableWidget);
        }
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

    public List<Rect2i> getBounds() {
        List<Rect2i> rects = new ArrayList<>();
        rects.add(this.redstoneWidget.getBounds());
        rects.add(this.upgradeWidget.getBounds());
        return rects;
    }

}
