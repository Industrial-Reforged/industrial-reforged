package com.indref.industrial_reforged.api.events;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class MultiblockUnformEvent extends MultiblockEvent {
    public MultiblockUnformEvent(Player player, BlockPos controllerPos, Multiblock multiblock) {
        super(player, controllerPos, multiblock);
    }

    public static class Pre extends MultiblockUnformEvent implements ICancellableEvent {
        public Pre(Player player, BlockPos controllerPos, Multiblock multiblock) {
            super(player, controllerPos, multiblock);
        }
    }

    public static class Post extends MultiblockUnformEvent {
        public Post(Player player, BlockPos controllerPos, Multiblock multiblock) {
            super(player, controllerPos, multiblock);
        }
    }

}
