package com.indref.industrial_reforged.api.events;

import com.indref.industrial_reforged.api.items.tools.DisplayItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

import java.util.List;

/**
 * Fired whenever the player scans a block. Clientside event
 */
public abstract class ScannerEvent extends Event {
    private final Player player;
    private final ItemStack scannerItem;
    private final List<Component> components;
    private final List<DisplayItem> compatibleItems;

    public ScannerEvent(Player player, ItemStack scannerItem, List<Component> components, List<DisplayItem> compatibleItems) {
        this.player = player;
        this.scannerItem = scannerItem;
        this.components = components;
        this.compatibleItems = compatibleItems;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getScannerItem() {
        return scannerItem;
    }

    public List<Component> getComponents() {
        return components;
    }

    public List<DisplayItem> getCompatibleItems() {
        return compatibleItems;
    }

    public static class ScanBlock extends ScannerEvent {
        private final BlockPos blockPos;

        public ScanBlock(Player player, BlockPos blockPos, ItemStack scannerItem, List<Component> components, List<DisplayItem> compatibleItems) {
            super(player, scannerItem, components, compatibleItems);
            this.blockPos = blockPos;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }
    }
}
