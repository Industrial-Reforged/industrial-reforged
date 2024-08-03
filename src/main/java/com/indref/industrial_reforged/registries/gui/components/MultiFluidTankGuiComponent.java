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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiFluidTankGuiComponent extends TooltipGuiComponent {
    private final FluidTankGuiComponent.TankVariants variant;
    private FluidTankRenderer renderer;
    private int tanks;

    public MultiFluidTankGuiComponent(@NotNull Vector2i position, FluidTankGuiComponent.TankVariants variant, int tanks) {
        super(position);
        this.variant = variant;
        this.tanks = tanks;
    }

    @Override
    protected void onInit() {
        super.onInit();
        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        // Need to initialize it here, since in the constructor we can't access the screen yet
        this.renderer = new FluidTankRenderer(CapabilityUtils.fluidHandlerCapability(blockEntity).getTankCapacity(0),
                true,
                textureWidth() - 2,
                textureHeight() - 2
        );
    }

    @Override
    public List<Component> getTooltip(int mouseX, int mouseY) {
        return Collections.emptyList();
    }

    @Override
    public int textureWidth() {
        return variant.textureWidth;
    }

    @Override
    public int textureHeight() {
        return variant.textureHeight;
    }

    public int fluidHeight() {
        return 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        int fluidHeight = 0;
        for (int i = 0; i < this.tanks; i++) {
            IFluidHandler fluidHandler = CapabilityUtils.fluidHandlerCapability(blockEntity);
            FluidStack fluid = fluidHandler.getFluidInTank(i);
            renderer.render(guiGraphics.pose(), position.x + 1, position.y + 1 + fluidHeight, fluid);
            int amount = fluid.getAmount();
            int capacity = fluidHandler.getTankCapacity(0);
            int height = textureHeight() - 2;
            fluidHeight -= (amount * height) / capacity;
        }
    }

    @Override
    public void renderInBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderInBackground(guiGraphics, mouseX, mouseY, delta);
        GuiUtils.drawImg(guiGraphics, variant.location, position.x, position.y, textureWidth(), textureHeight());
    }
}
