package com.indref.industrial_reforged.registries.gui.components;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.gui.components.GuiComponent;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.CapabilityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class HeatDisplayGuiComponent extends GuiComponent {
    private final boolean requiresDisplayItem;

    public HeatDisplayGuiComponent(@NotNull Vector2i position, boolean requiresDisplayItem) {
        super(position);
        this.requiresDisplayItem = requiresDisplayItem;
    }

    @Override
    public int textureWidth() {
        return 0;
    }

    @Override
    public int textureHeight() {
        return 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (requiresDisplayItem) {
            Inventory inv = screen.getMenu().getInv();
            for (ItemStack stack : inv.items) {
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
        ContainerBlockEntity blockEntity = screen.getMenu().getBlockEntity();
        IHeatStorage heatStorage = CapabilityUtils.heatStorageCapability(blockEntity);
        if (heatStorage != null) {
            int temperature = heatStorage.getHeatStored();

            guiGraphics.drawString(Minecraft.getInstance().font,
                    Component.literal("Temperature: " + temperature + "°C").withStyle(ChatFormatting.WHITE),
                    position.x,
                    position.y,
                    0);
        }
    }
}
