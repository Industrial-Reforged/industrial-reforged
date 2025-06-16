package com.indref.industrial_reforged.client.widgets;

import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.function.Consumer;

public record MachineWidgetContext(MachineContainerMenu<?> menu, Consumer<PanelWidget> onWidgetResizeFunc) {
}
