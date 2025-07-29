package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.menus.CraftingStationMenu;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CraftingStationScreen extends PDLAbstractContainerScreen<CraftingStationMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/crafting_station.png");

    public CraftingStationScreen(CraftingStationMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageHeight = 209;
        this.inventoryLabelY = -1000;
        this.imageWidth = 178;
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
