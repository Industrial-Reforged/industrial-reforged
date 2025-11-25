package com.indref.industrial_reforged.client.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.impl.energy.IRGenericEnergyWrapper;
import com.indref.industrial_reforged.api.client.screen.MachineScreen;
import com.indref.industrial_reforged.client.widgets.EnergyBarWidget;
import com.indref.industrial_reforged.client.widgets.RedstonePanelWidget;
import com.indref.industrial_reforged.content.menus.BatteryBoxMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BatteryBoxScreen extends MachineScreen<BatteryBoxMenu> {

    public static final ResourceLocation TEXTURE = IndustrialReforged.rl("textures/gui/battery_box.png");

    public BatteryBoxScreen(BatteryBoxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.imageHeight = 186;
        this.inventoryLabelY = this.imageHeight - 94;
        super.init();
        EnergyBarWidget energyBarWidget = addRenderableOnly(
                new EnergyBarWidget(this.leftPos + (this.imageWidth - 12) / 2, this.topPos + 28, new IRGenericEnergyWrapper(menu.blockEntity.getEuStorage()), true)
        );
        addPanelWidget(new RedstonePanelWidget(this.leftPos + this.imageWidth, this.topPos + 2));
        //addRenderableOnly(new BatterySlotWidget(this.leftPos + (this.imageWidth - 16) / 2, this.topPos + 14 + energyBarWidget.getHeight() + 5));

    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
