package com.indref.industrial_reforged.registries.gui.components;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.gui.components.TooltipGuiComponent;
import com.indref.industrial_reforged.util.renderer.GuiUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public class EnergyGuiComponent extends TooltipGuiComponent {
    private static final ResourceLocation ENERGY_BAR = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/energy_bar.png");
    private static final ResourceLocation ENERGY_BAR_EMPTY = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/energy_bar_empty.png");

    private final boolean addBatterySlot;

    public EnergyGuiComponent(@NotNull Vector2i position, boolean addBatterySlot) {
        super(position);
        this.addBatterySlot = addBatterySlot;
    }

    @Override
    public List<Component> getTooltip() {
        ContainerBlockEntity blockEntity = screen.getMenu().getBlockEntity();
        return List.of(
                Component.literal(blockEntity.getEnergyStored() + "/" + blockEntity.getEnergyCapacity())
        );
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
        // This rendering is kinda weird, it always renders the regular energybar
        // and over that it renders the empty bar. The empty energy bar is `full`
        // and when the energy starts filling up, the empty energy bar lifts to
        // show the regular energy bar underneath
        super.render(guiGraphics, mouseX, mouseY, delta);
        GuiUtils.drawImg(guiGraphics, ENERGY_BAR, position.x, position.y, textureWidth(), textureHeight());

        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        int energyStored = blockEntity.getEnergyStored();
        int maxStored = blockEntity.getEnergyCapacity();

        float percentage = 1f - (float) energyStored / maxStored;
        int j1 = Mth.ceil(percentage * textureHeight());
        guiGraphics.blit(ENERGY_BAR_EMPTY, position.x, position.y, textureWidth(), j1, 0, 0, textureWidth(), j1, textureWidth(), textureHeight());
    }

    @Override
    public void renderInBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderInBackground(guiGraphics, mouseX, mouseY, delta);
        if (this.addBatterySlot) {
            GuiUtils.renderBatterySlot(guiGraphics, position.x - 2, position.y + textureHeight() + 2);
        }
    }
}
