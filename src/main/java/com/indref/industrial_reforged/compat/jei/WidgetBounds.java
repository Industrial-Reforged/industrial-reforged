package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.api.client.screen.MachineScreen;
import com.indref.industrial_reforged.client.screen.CentrifugeScreen;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;

import java.util.List;

public class WidgetBounds implements IGuiContainerHandler<PDLAbstractContainerScreen<?>> {
    @Override
    public List<Rect2i> getGuiExtraAreas(PDLAbstractContainerScreen<?> containerScreen) {
        if (containerScreen instanceof MachineScreen<?> screen) {
            return screen.getBounds();
        }
        return IGuiContainerHandler.super.getGuiExtraAreas(containerScreen);
    }
}