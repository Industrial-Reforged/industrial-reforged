package com.indref.industrial_reforged.api.events;

import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;

public abstract class MultiblockEvent extends Event {
    private final Player player;
    private final BlockPos controllerPos;
    private final Multiblock multiblock;

    public MultiblockEvent(Player player, BlockPos controllerPos, Multiblock multiblock) {
        this.player = player;
        this.controllerPos = controllerPos;
        this.multiblock = multiblock;
    }

    public Player getPlayer() {
        return player;
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public Multiblock getMultiblock() {
        return multiblock;
    }
}
