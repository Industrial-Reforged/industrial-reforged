package com.indref.industrial_reforged.client.item;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.util.IRHooks;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public final class IRDisplayItems {
    public static void scanner(GuiGraphics guiGraphics, int x, int y, int lineOffset, Level level, Player player, BlockPos blockPos, ItemStack itemStack) {
        Font font = Minecraft.getInstance().font;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        BlockState blockstate = level.getBlockState(blockPos);
        if (blockEntity != null) {
            IEnergyStorage energyStorage = CapabilityUtils.energyStorageCapability(blockEntity);
            if (energyStorage != null) {
                List<Component> components = new ArrayList<>();
                List<ItemLike> compatibleItems = new ArrayList<>();
                DisplayBlock displayBlock = null;
                // Collect display text
                IRHooks.scanBlock(player, blockPos, itemStack, components, compatibleItems);
                if (blockstate.getBlock() instanceof DisplayBlock dBlock) {
                    displayBlock = dBlock;
                    compatibleItems.addAll(dBlock.getCompatibleItems());
                }

                if (displayBlock != null && compatibleItems.contains(itemStack.getItem())) {
                    displayBlock.displayOverlay(components, player, level, itemStack, blockPos, blockstate);
                }

                // Render display if there is one
                if (!components.isEmpty()) {
                    for (Component component : components) {
                        guiGraphics.drawCenteredString(font, component, x, y + lineOffset, 256);
                        lineOffset += font.lineHeight + 3;
                    }
                }
                // Drain energy
                if (level.getGameTime() % 20 == 0 && !player.isCreative()) {
                    // TODO: Make scanner only work when it has enough energy
                    //int drained = this.tryDrainEnergy(itemStack, 1);
                }
            }
        }
    }

    public static void thermometer(GuiGraphics guiGraphics, int x, int y, int lineOffset, Level level, Player player, BlockPos blockPos, ItemStack itemStack) {
        Font font = Minecraft.getInstance().font;
        BlockState blockstate = level.getBlockState(blockPos);
        if (blockstate.getBlock() instanceof DisplayBlock displayBlock) {
            if (!displayBlock.getCompatibleItems().contains(itemStack.getItem())) return;

            List<Component> displayText = new ArrayList<>();

            displayBlock.displayOverlay(displayText, player, level, itemStack, blockPos, blockstate);

            for (Component component : displayText) {
                guiGraphics.drawCenteredString(font, component, x, y + lineOffset, 256);
                lineOffset += font.lineHeight + 3;
            }
        }
    }
}
