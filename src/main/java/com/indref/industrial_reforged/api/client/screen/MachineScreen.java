package com.indref.industrial_reforged.api.client.screen;

import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.client.widgets.MachineWidgetContext;
import com.indref.industrial_reforged.api.client.widget.PanelWidget;
import com.indref.industrial_reforged.networking.UpgradeWidgetSetSlotPositionsPayload;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class MachineScreen<T extends MachineContainerMenu<?>> extends PDLAbstractContainerScreen<T> {
    protected final List<PanelWidget> panelWidgets;

    public MachineScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.panelWidgets = new ArrayList<>();
    }

    @Override
    protected void init() {
        super.init();
    }

    public void addPanelWidget(PanelWidget widget) {
        widget.visitWidgets(this::addRenderableWidget);
        this.panelWidgets.add(widget);
        widget.setContext(new MachineWidgetContext(this.menu, this::onWidgetResize));
    }

    private void onWidgetResize(PanelWidget widget) {
        List<PanelWidget> widgetsToResize = this.panelWidgets.stream().filter(widget1 -> widget1.getY() > widget.getY()).toList();
        for (PanelWidget widget1 : widgetsToResize) {
            if (!widget.isOpen()){
                widget1.setY(widget1.getOriginalY());
            } else {
                widget1.setY(widget.getY() + widget.getOpenHeight() + 2);
            }
            widget1.onWidgetResized(widget);
        }
    }

}
