package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class MultiblockHelper {
    public static void form(IMultiBlockController controller, BlockPos controllerPos, Player player) {
        if (controller.getMultiblock().isValid()) {
            player.sendSystemMessage(Component.literal("Simulating forming"));
        }
    }
}
