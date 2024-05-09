package com.indref.industrial_reforged.api.gui.components;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.util.GuiUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public class EnergyGuiComponent extends TooltipGuiComponent {
    private static final ResourceLocation ENERGY_BAR = new ResourceLocation(IndustrialReforged.MODID, "textures/gui/energy_bar.png");
    private static final ResourceLocation ENERGY_BAR_EMPTY = new ResourceLocation(IndustrialReforged.MODID, "textures/gui/energy_bar_empty.png");

    public EnergyGuiComponent(@NotNull Vector2i position) {
        super(position);
    }

    @Override
    public List<Component> getTooltip() {
        return List.of(
                Component.literal("Energy stored: "+screen.getMenu().getBlockEntity().getEnergyStored())
        );
    }

    @Override
    public int textureWidth() {
        return 12;
    }

    @Override
    public int textureHeight() {
        return 64;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        GuiUtils.drawImg(guiGraphics, ENERGY_BAR, position.x, position.y, textureWidth(), textureHeight());

        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        int energyStored = blockEntity.getEnergyStored();
        int maxStored = blockEntity.getEnergyCapacity();

        float percentage = (float) energyStored / maxStored;
    }
}
