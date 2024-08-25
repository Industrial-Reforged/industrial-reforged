package com.indref.industrial_reforged.api.events;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class MultiblockFormEvent extends MultiblockEvent {
    public MultiblockFormEvent(Player player, BlockPos controllerPos, Multiblock multiblock) {
        super(player, controllerPos, multiblock);
    }

    public static class Pre extends MultiblockFormEvent implements ICancellableEvent {
        private final MultiblockData multiblockData;

        public Pre(Player player, BlockPos controllerPos, Multiblock multiblock, MultiblockData multiblockData) {
            super(player, controllerPos, multiblock);
            this.multiblockData = multiblockData;
        }

        public MultiblockData getMultiblockData() {
            return multiblockData;
        }
    }

    public static class Post extends MultiblockFormEvent {
        public Post(Player player, BlockPos controllerPos, Multiblock multiblock) {
            super(player, controllerPos, multiblock);
        }
    }
}
