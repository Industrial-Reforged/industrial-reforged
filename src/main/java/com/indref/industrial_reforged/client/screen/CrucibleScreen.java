package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.content.gui.menus.CrucibleMenu;
import com.indref.industrial_reforged.content.gui.widgets.HeatDisplayWidget;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import com.portingdeadmods.portingdeadlibs.impl.client.screens.widgets.FluidTankWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class CrucibleScreen extends PDLAbstractContainerScreen<CrucibleMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/crucible.png");

    public CrucibleScreen(CrucibleMenu menu, Inventory inv, Component name) {
        super(menu, inv, name);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new FluidTankWidget(leftPos + 98, this.topPos + 17, FluidTankWidget.TankVariants.LARGE, menu.blockEntity));
        addRenderableWidget(new HeatDisplayWidget((width - imageWidth) / 2, (height - imageHeight) / 2, menu.blockEntity, menu.getInv(), true));
    }


    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
