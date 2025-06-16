package com.indref.industrial_reforged.api.client.screen;

import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.client.widgets.MachineWidgetContext;
import com.indref.industrial_reforged.client.widgets.PanelWidget;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class MachineScreen<T extends MachineContainerMenu<?>> extends PDLAbstractContainerScreen<T> {
    private final List<PanelWidget> panelWidgets;

    public MachineScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.panelWidgets = new ArrayList<>();
    }

    public void addPanelWidget(PanelWidget widget) {
        this.addRenderableWidget(widget);
        this.panelWidgets.add(widget);
        widget.setContext(new MachineWidgetContext(this.menu, this::onWidgetResize));
    }

    private void onWidgetResize(PanelWidget widget) {
        List<PanelWidget> widgetsToResize = this.panelWidgets.stream().filter(widget1 -> widget1.getY() > widget.getY()).toList();
        for (PanelWidget widget1 : widgetsToResize) {
            if (!widget.isOpen()){
                widget1.setY(widget1.getOriginalY());
            } else {
                widget1.setY(widget1.getOriginalY() + widget.getHeight());
            }
        }
    }

}
