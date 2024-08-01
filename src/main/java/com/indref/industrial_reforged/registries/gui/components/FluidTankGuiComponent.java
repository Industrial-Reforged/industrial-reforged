package com.indref.industrial_reforged.registries.gui.components;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.gui.FluidTankRenderer;
import com.indref.industrial_reforged.api.gui.components.TooltipGuiComponent;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.indref.industrial_reforged.util.renderer.GuiUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public class FluidTankGuiComponent extends TooltipGuiComponent {
    private static final ResourceLocation SMALL_TANK = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/sprites/small_tank.png");
    private static final ResourceLocation NORMAL_TANK = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/sprites/normal_tank.png");
    private static final ResourceLocation LARGE_TANK = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/sprites/large_tank.png");

    private final TankVariants variant;
    private FluidTankRenderer renderer;

    public FluidTankGuiComponent(@NotNull Vector2i position, TankVariants variant) {
        super(position);
        this.variant = variant;
    }

    @Override
    protected void onInit() {
        super.onInit();
        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        // Need to initialize it here, since in the constructor we can't access the screen yet
        this.renderer = new FluidTankRenderer(CapabilityUtils.fluidHandlerCapability(blockEntity).getTankCapacity(0), true, textureWidth()-2, textureHeight()-2);
    }

    @Override
    public List<Component> getTooltip() {
        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        return renderer.getTooltip(CapabilityUtils.fluidHandlerCapability(blockEntity).getFluidInTank(0));
    }

    @Override
    public int textureWidth() {
        return variant.textureWidth;
    }

    @Override
    public int textureHeight() {
        return variant.textureHeight;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        renderer.render(guiGraphics.pose(), position.x + 1, position.y + 1, CapabilityUtils.fluidHandlerCapability(blockEntity).getFluidInTank(0));
    }

    @Override
    public void renderInBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderInBackground(guiGraphics, mouseX, mouseY, delta);
        GuiUtils.drawImg(guiGraphics, variant.location, position.x, position.y, textureWidth(), textureHeight());
    }

    public enum TankVariants {
        SMALL(18, 54, SMALL_TANK),
        NORMAL(36, 54, NORMAL_TANK),
        LARGE(54, 54, LARGE_TANK);

        final int textureWidth;
        final int textureHeight;
        final ResourceLocation location;

        TankVariants(int textureWidth, int textureHeight, ResourceLocation location) {
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.location = location;
        }
    }
}
