package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.events.MultiblockFormEvent;
import com.indref.industrial_reforged.api.events.MultiblockUnformEvent;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;

public final class IRHooks {
    public static boolean preMultiblockFormed(Multiblock multiblock, Player player, BlockPos controllerPos, MultiblockData multiblockData) {
        return NeoForge.EVENT_BUS.post(new MultiblockFormEvent.Pre(player, controllerPos, multiblock, multiblockData)).isCanceled();
    }
    
    public static void postMultiblockFormed(Multiblock multiblock, Player player, BlockPos controllerPos) {
        NeoForge.EVENT_BUS.post(new MultiblockFormEvent.Post(player, controllerPos, multiblock));
    }

    public static boolean preMultiblockUnformed(Multiblock multiblock, Player player, BlockPos controllerPos) {
        return NeoForge.EVENT_BUS.post(new MultiblockUnformEvent.Pre(player, controllerPos, multiblock)).isCanceled();
    }

    public static void postMultiblockUnformed(Multiblock multiblock, Player player, BlockPos controllerPos) {
        NeoForge.EVENT_BUS.post(new MultiblockUnformEvent.Post(player, controllerPos, multiblock));
    }
    
}
