package com.indref.industrial_reforged.registries.gui.screens;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.IRAbstractContainerScreen;
import com.indref.industrial_reforged.registries.gui.components.FluidTankGuiComponent;
import com.indref.industrial_reforged.registries.gui.components.HeatDisplayGuiComponent;
import com.indref.industrial_reforged.registries.gui.components.MultiFluidTankGuiComponent;
import com.indref.industrial_reforged.registries.gui.menus.CrucibleMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class CrucibleScreen extends IRAbstractContainerScreen<CrucibleMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/crucible.png");

    public CrucibleScreen(CrucibleMenu menu, Inventory inv, Component name) {
        super(menu, inv, name);
    }

    @Override
    protected void init() {
        super.init();
        initComponents(
                new MultiFluidTankGuiComponent(new Vector2i(leftPos + 98, this.topPos + 17), FluidTankGuiComponent.TankVariants.LARGE, 2),
                new HeatDisplayGuiComponent(new Vector2i((width - imageWidth) / 2, (height - imageHeight) / 2), true)
        );
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
