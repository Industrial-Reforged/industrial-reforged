package com.indref.industrial_reforged.content.gui.widgets;

import com.indref.industrial_reforged.api.blockentities.container.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class HeatDisplayWidget extends AbstractWidget {
    private final Inventory playerInventory;
    private final IHeatStorage heatStorage;
    private final boolean requiresDisplayItem;

    public HeatDisplayWidget(int x, int y, IRContainerBlockEntity blockEntity, Inventory playerInventory, boolean requiresDisplayItem) {
        this(x, y, blockEntity.getHeatStorage(), playerInventory, requiresDisplayItem);
    }

    public HeatDisplayWidget(int x, int y, IHeatStorage heatStorage, Inventory playerInventory, boolean requiresDisplayItem) {
        super(x, y, 0, 0, CommonComponents.EMPTY);
        this.heatStorage = heatStorage;
        this.requiresDisplayItem = requiresDisplayItem;
        this.playerInventory = playerInventory;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (requiresDisplayItem) {
            for (ItemStack stack : playerInventory.items) {
                if (stack.is(IRItems.THERMOMETER.get())) {
                    renderHeatDisplay(guiGraphics);
                    break;
                }
            }
        } else {
            renderHeatDisplay(guiGraphics);
        }
    }

    private void renderHeatDisplay(GuiGraphics guiGraphics) {
        if (heatStorage != null) {
            int temperature = heatStorage.getHeatStored();

            guiGraphics.drawString(Minecraft.getInstance().font,
                    Component.literal("Temperature: " + temperature + "°C").withStyle(ChatFormatting.WHITE),
                    getX(),
                    getY(),
                    -1);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
