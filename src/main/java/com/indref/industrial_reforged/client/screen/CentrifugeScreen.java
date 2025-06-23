package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.energy.IREnergyStorageWrapper;
import com.indref.industrial_reforged.api.client.screen.MachineScreen;
import com.indref.industrial_reforged.api.client.widget.PanelWidget;
import com.indref.industrial_reforged.api.gui.slots.UpgradeSlot;
import com.indref.industrial_reforged.client.widgets.*;
import com.indref.industrial_reforged.content.gui.menus.CentrifugeMenu;
import com.indref.industrial_reforged.networking.UpgradeWidgetSetSlotPositionsPayload;
import com.portingdeadmods.portingdeadlibs.impl.client.screens.widgets.FluidTankWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CentrifugeScreen extends MachineScreen<CentrifugeMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/centrifuge.png");
    private static final ResourceLocation PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "container/centrifuge/progress_arrows");

    public CentrifugeScreen(CentrifugeMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();

        this.imageHeight = 185;
        this.inventoryLabelY = this.imageHeight - 94;
        EnergyBarWidget energyBarWidget = addRenderableOnly(
                new EnergyBarWidget(this.leftPos + 11, this.topPos + 16, new IREnergyStorageWrapper(menu.blockEntity.getEuStorage()), true)
        );
        addRenderableWidget(
                new FluidTankWidget(this.leftPos + 142, this.topPos + 24, FluidTankWidget.TankVariants.SMALL, menu.blockEntity)
        );
        addRenderableOnly(new BatterySlotWidget(this.leftPos + 8, this.topPos + 14 + energyBarWidget.getHeight() + 4));

        addPanelWidget(new RedstonePanelWidget(this.leftPos + this.imageWidth, this.topPos + 2));
        addPanelWidget(new UpgradePanelWidget(this.leftPos + this.imageWidth, this.topPos + 2 + 24 + 2));

        PacketDistributor.sendToServer(new UpgradeWidgetSetSlotPositionsPayload(51));
        this.menu.setUpgradeSlotPositions(51);

        for (UpgradeSlot upgradeSlot : this.menu.getUpgradeSlots()) {
            upgradeSlot.setActive(false);
        }
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, delta, mouseX, mouseY);
        float progress = this.menu.blockEntity.getProgress() / this.menu.blockEntity.getMaxProgress();
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
