package com.indref.industrial_reforged.client.widgets;

import com.indref.industrial_reforged.translations.IRTranslations;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.capabilities.EnergyStorageWrapper;
import com.portingdeadmods.portingdeadlibs.api.capabilities.NeoEnergyStorageWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class EnergyBarWidget extends AbstractWidget {
    private static final ResourceLocation ENERGY_BAR = PortingDeadLibs.rl("energy_bar");
    private static final ResourceLocation ENERGY_BAR_EMPTY = PortingDeadLibs.rl("energy_bar_empty");
    private static final ResourceLocation ENERGY_BAR_BORDER = PortingDeadLibs.rl("energy_bar_border");
    private static final ResourceLocation ENERGY_BAR_EMPTY_BORDER = PortingDeadLibs.rl("energy_bar_empty_border");
    private final boolean hasBorder;
    private final EnergyStorageWrapper wrapper;

    public EnergyBarWidget(int x, int y, EnergyStorageWrapper wrapper, boolean hasBorder) {
        super(x, y, 12, 48, CommonComponents.EMPTY);
        this.hasBorder = hasBorder;
        this.wrapper = wrapper;
    }

    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        ResourceLocation loc = this.hasBorder ? ENERGY_BAR_EMPTY_BORDER : ENERGY_BAR_EMPTY;
        guiGraphics.blitSprite(loc, this.width, this.height, 0, 0, this.getX(), this.getY(), this.width, this.height);
        int energyStored = this.wrapper.getEnergyStored();
        int maxStored = this.wrapper.getEnergyCapacity();
        int progress = (int)((float)this.height * ((float)energyStored / (float)maxStored));
        ResourceLocation locFull = this.hasBorder ? ENERGY_BAR_BORDER : ENERGY_BAR;
        guiGraphics.blitSprite(locFull, this.width, this.height, 0, this.height - progress, this.getX(), this.getY() + this.height - progress, this.width, progress);
        if (this.isHovered()) {
            Font font = Minecraft.getInstance().font;
            guiGraphics.renderTooltip(font, Component.literal(energyStored + "/" + maxStored)
                    .append(" ")
                    .append(IRTranslations.General.ENERGY_UNIT.component())
                    .withColor(FastColor.ARGB32.color(210, 65, 30)), mouseX, mouseY);
        }

    }

    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}