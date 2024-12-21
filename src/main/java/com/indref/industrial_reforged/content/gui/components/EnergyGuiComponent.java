package com.indref.industrial_reforged.content.gui.components;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.gui.components.TooltipGuiComponent;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.indref.industrial_reforged.util.renderer.GuiUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public class EnergyGuiComponent extends TooltipGuiComponent {
    private static final ResourceLocation ENERGY_BAR =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "energy_bar");
    private static final ResourceLocation ENERGY_BAR_EMPTY =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "energy_bar_empty");
    private static final ResourceLocation ENERGY_BAR_BORDER =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "energy_bar_border");
    private static final ResourceLocation ENERGY_BAR_EMPTY_BORDER =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "energy_bar_empty_border");

    private final boolean addBatterySlot;
    private final boolean hasBorder;

    public EnergyGuiComponent(@NotNull Vector2i position, boolean addBatterySlot, boolean hasBorder) {
        super(position);
        this.addBatterySlot = addBatterySlot;
        this.hasBorder = hasBorder;
    }

    @Override
    public List<Component> getTooltip(int mouseX, int mouseY) {
        ContainerBlockEntity blockEntity = screen.getMenu().getBlockEntity();
        IEnergyStorage energyStorage = CapabilityUtils.energyStorageCapability(blockEntity);
        if (energyStorage != null) {
            return List.of(
                    Component.literal(energyStorage.getEnergyStored() + "/" + energyStorage.getEnergyCapacity())
            );
        } else {
            return List.of();
        }
    }

    @Override
    public int textureWidth() {
        return 12;
    }

    @Override
    public int textureHeight() {
        return 48;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        ResourceLocation loc = hasBorder ? ENERGY_BAR_EMPTY_BORDER : ENERGY_BAR_EMPTY;
        int height = textureHeight();
        int width = textureWidth();
        guiGraphics.blitSprite(loc, width, height, 0, 0, position.x, position.y, width, height);

        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        IEnergyStorage energyStorage = blockEntity.getEnergyStorage();
        if (energyStorage != null) {
            int energyStored = energyStorage.getEnergyStored();
            int maxStored = energyStorage.getEnergyCapacity();

            int progress = (int) (height *  ((float) energyStored / maxStored));
            ResourceLocation locFull = hasBorder ? ENERGY_BAR_BORDER : ENERGY_BAR;
            guiGraphics.blitSprite(locFull, width, height, 0, height - progress, position.x, position.y + height - progress, width, progress);
        }
    }

    @Override
    public void renderInBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderInBackground(guiGraphics, mouseX, mouseY, delta);
        if (this.addBatterySlot) {
            GuiUtils.renderBatterySlot(guiGraphics, position.x - 2, position.y + textureHeight() + 2);
        }
    }
}
