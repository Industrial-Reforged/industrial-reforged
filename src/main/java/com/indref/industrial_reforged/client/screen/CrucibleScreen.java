package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.widgets.ClearableFluidTankWidget;
import com.indref.industrial_reforged.client.widgets.HeatBarWidget;
import com.indref.industrial_reforged.content.gui.menus.CrucibleMenu;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import com.portingdeadmods.portingdeadlibs.impl.client.screens.widgets.FluidTankWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CrucibleScreen extends PDLAbstractContainerScreen<CrucibleMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/crucible.png");

    public CrucibleScreen(CrucibleMenu menu, Inventory inv, Component name) {
        super(menu, inv, name);
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new ClearableFluidTankWidget(leftPos + 98, this.topPos + 17, FluidTankWidget.TankVariants.LARGE, menu.blockEntity));
        addRenderableWidget(new HeatBarWidget(menu.blockEntity.getHeatStorage(), this.leftPos + 89, this.topPos + 73));
    }


    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }

}
