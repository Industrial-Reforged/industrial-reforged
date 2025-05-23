package com.indref.industrial_reforged.api.events;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.Event;

import java.util.List;

/**
 * Fired whenever the player scans a block. Clientside event
 */
public abstract class ScannerEvent extends Event {
    private final Player player;
    private final ItemStack scannerItem;
    private final List<Component> components;
    private final List<ItemLike> compatibleItems;

    public ScannerEvent(Player player, ItemStack scannerItem, List<Component> components, List<ItemLike> compatibleItems) {
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

    public List<ItemLike> getCompatibleItems() {
        return compatibleItems;
    }

    public static class ScanBlock extends ScannerEvent {
        private final BlockPos blockPos;

        public ScanBlock(Player player, BlockPos blockPos, ItemStack scannerItem, List<Component> components, List<ItemLike> compatibleItems) {
            super(player, scannerItem, components, compatibleItems);
            this.blockPos = blockPos;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }
    }
}
