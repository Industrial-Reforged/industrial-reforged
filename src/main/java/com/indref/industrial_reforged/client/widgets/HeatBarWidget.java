package com.indref.industrial_reforged.client.widgets;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.translations.IRTranslations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HeatBarWidget extends AbstractWidget {
    public static final ResourceLocation HEAT_BAR_SPRITE = IndustrialReforged.rl("heat_bar");
    public static final ResourceLocation HEAT_BAR_EMPTY_SPRITE = IndustrialReforged.rl("heat_bar_empty");

    private final IHeatStorage heatStorage;

    public HeatBarWidget(IHeatStorage heatStorage, int x, int y) {
        super(x, y, 74, 4, CommonComponents.EMPTY);
        this.heatStorage = heatStorage;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(HEAT_BAR_EMPTY_SPRITE, getX(), getY(), width, height);
        float heatStored = heatStorage.getHeatStored();
        float heatCapacity = heatStorage.getHeatCapacity();
        float progress = heatStored / heatCapacity;
        guiGraphics.blitSprite(HEAT_BAR_SPRITE, width - 2, height - 2, 0, 0, getX() + 1, getY() + 1, Mth.ceil((width - 2) * progress), height - 2);

        if (isHovered) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, IRTranslations.Tooltip.HEAT_AMOUNT_WITH_CAPACITY.component(heatStored, heatCapacity).append(IRTranslations.General.HEAT_UNIT.component()), mouseX, mouseY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
    }
}
