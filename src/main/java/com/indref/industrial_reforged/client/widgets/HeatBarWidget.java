package com.indref.industrial_reforged.client.widgets;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import com.indref.industrial_reforged.translations.IRTranslations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HeatBarWidget extends AbstractWidget {
    public static final ResourceLocation HEAT_BAR_SPRITE = IndustrialReforged.rl("heat_bar");
    public static final ResourceLocation HEAT_BAR_EMPTY_SPRITE = IndustrialReforged.rl("heat_bar_empty");

    private final HeatStorage heatStorage;

    public HeatBarWidget(HeatStorage heatStorage, int x, int y) {
        super(x, y, 80, 8, CommonComponents.EMPTY);
        this.heatStorage = heatStorage;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(HEAT_BAR_EMPTY_SPRITE, getX(), getY(), width, height);
        float heatStored = heatStorage.getHeatStored();
        float heatCapacity = heatStorage.getHeatCapacity();
        float progress = heatStored / heatCapacity;
        guiGraphics.blitSprite(HEAT_BAR_SPRITE, width, height, 0, 0, getX(), getY(), Mth.ceil(width * progress), height);

        if (isHovered) {
            float roundedHeat = Math.round(heatStored * 10) / 10.0f;
            float roundedCapacity = Math.round(heatCapacity * 10) / 10.0f;
            guiGraphics.renderTooltip(Minecraft.getInstance().font, IRTranslations.Tooltip.HEAT_AMOUNT_WITH_CAPACITY.component(roundedHeat, roundedCapacity)
                    .append(IRTranslations.General.HEAT_UNIT.component()), mouseX, mouseY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
    }
}
