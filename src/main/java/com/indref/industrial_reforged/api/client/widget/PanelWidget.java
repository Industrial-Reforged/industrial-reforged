package com.indref.industrial_reforged.api.client.widget;

import com.indref.industrial_reforged.client.widgets.MachineWidgetContext;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

public abstract class PanelWidget extends AbstractWidget {
    protected MachineWidgetContext context;
    protected boolean open;
    private final int originalX;
    private final int originalY;
    private final int openWidth;
    private final int openHeight;
    private final int closedWidth;
    private final int closedHeight;

    public PanelWidget(int x, int y, int openWidth, int openHeight, int closedWidth, int closedHeight) {
        super(x, y, closedWidth, closedHeight, Component.empty());
        this.originalX = x;
        this.originalY = y;
        this.openWidth = openWidth;
        this.openHeight = openHeight;
        this.closedWidth = closedWidth;
        this.closedHeight = closedHeight;
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

    public int getClosedWidth() {
        return closedWidth;
    }

    public int getClosedHeight() {
        return closedHeight;
    }

    public int getOpenWidth() {
        return openWidth;
    }

    public int getOpenHeight() {
        return openHeight;
    }

    public int getOriginalX() {
        return originalX;
    }

    public int getOriginalY() {
        return originalY;
    }

    public void onWidgetResized(PanelWidget resizedWidget) {
    }

    public Rect2i getBounds() {
        return new Rect2i(getX(), getY(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
