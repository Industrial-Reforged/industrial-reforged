package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.IRAbstractContainerScreen;
import com.indref.industrial_reforged.content.gui.components.FluidTankGuiComponent;
import com.indref.industrial_reforged.content.gui.components.HeatDisplayGuiComponent;
import com.indref.industrial_reforged.content.gui.menus.CrucibleMenu;
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
                new FluidTankGuiComponent(new Vector2i(leftPos + 98, this.topPos + 17), FluidTankGuiComponent.TankVariants.LARGE),
                new HeatDisplayGuiComponent(new Vector2i((width - imageWidth) / 2, (height - imageHeight) / 2), true)
        );
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
