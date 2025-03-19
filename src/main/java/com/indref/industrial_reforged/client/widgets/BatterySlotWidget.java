package com.indref.industrial_reforged.client.widgets;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

public class BatterySlotWidget extends AbstractWidget {
    public static final ResourceLocation BATTERY_SLOT_SPRITE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "battery_slot");

    public BatterySlotWidget(int x, int y) {
        super(x, y, 18, 18, CommonComponents.EMPTY);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(BATTERY_SLOT_SPRITE, getX(), getY(), width, height);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
    }

}
