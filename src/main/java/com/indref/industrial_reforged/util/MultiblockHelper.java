package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.mojang.datafixers.util.Pair;
import com.sun.jna.platform.WindowUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.Map;

public class MultiblockHelper {
    public static boolean isValid(IMultiBlockController controller, BlockPos controllerPos, Level level, Player player) {
        IMultiblock multiblock = controller.getMultiblock();
        Map<Integer, Block> def = multiblock.getDefinition();
        List<Integer> layout = multiblock.getLayout().get(0);
        Pair<Integer, Integer> relativeControllerPos = getControllerRelativePos(controller);
        for (int index : layout) {
            if (def.get(layout.get(index)).equals(level.getBlockState(new BlockPos(controllerPos.getX() - 1, controllerPos.getY(), controllerPos.getZ() + 1)).getBlock())) {
                // player.sendSystemMessage(Component.literal("Block is valid: " + new BlockPos(controllerPos.getX() - 1, controllerPos.getY(), controllerPos.getZ() + 1)));
            } else {
                // player.sendSystemMessage(Component.literal("!! Not valid: " + level.getBlockState(new BlockPos(controllerPos.getX() - 1, controllerPos.getY(), controllerPos.getZ() + 1)).getBlock()));
            }
        }
        player.sendSystemMessage(Component.literal("Controller rel pos: "+relativeControllerPos));
        return false;
    }

    /**
     * @return x, z
     */
    public static Pair<Integer, Integer> getControllerRelativePos(IMultiBlockController controller) {
        Map<Block, Integer> reverseDef = Util.reverseMap(controller.getMultiblock().getDefinition());
        List<Integer> layout = controller.getMultiblock().getLayout().get(0);
        int x = 1;
        int z = 0;
        for (int blockIndex : layout) {
            if (x <= controller.getMultiblock().getWidths().get(0)) {
                x++;
            } else {
                x = 0;
                z++;
            }
            if (blockIndex == reverseDef.get(controller.getMultiblock().getController())) {
                return Pair.of(x, z);
            }
        }
        return null;
    }

    public static void form(IMultiBlockController controller, BlockPos controllerPos, Level level, Player player) {
        if (isValid(controller, controllerPos, level, player)) {
            player.sendSystemMessage(Component.literal("Simulating forming"));
        }
    }
}
