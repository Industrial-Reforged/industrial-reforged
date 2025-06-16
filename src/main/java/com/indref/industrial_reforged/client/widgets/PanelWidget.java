package com.indref.industrial_reforged.client.widgets;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class PanelWidget extends AbstractWidget {
    protected MachineWidgetContext context;
    protected boolean open;
    private final int originalX;
    private final int originalY;

    public PanelWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.originalX = x;
        this.originalY = y;
    }

    public void setContext(MachineWidgetContext context) {
        this.context = context;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getOriginalX() {
        return originalX;
    }

    public int getOriginalY() {
        return originalY;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
