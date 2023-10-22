package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

public class MultiblockHelper {
    public static boolean isValid(IMultiBlockController controller, BlockPos controllerPos, Level level, Player player) {
        IMultiblock multiblock = controller.getMultiblock();
        Map<Integer, Block> def = multiblock.getDefinition();
        Map<Block, Integer> reverseDef = Util.reverseMap(multiblock.getDefinition());
        List<Integer> layout = multiblock.getLayout().get(0);
        Pair<Integer, Integer> relativeControllerPos = getControllerRelativePos(controller);
        int index = 0;
        if (relativeControllerPos == null) {
            throw new NullPointerException("Relative controller pos is not available. May be caused due to a multiblock layout without a controller");
        }

        // Calculate block pos of the first block in the multi (multiblock.getLayout().get(0))
        int firstBlockPosX = controllerPos.getX() - relativeControllerPos.getSecond();
        int blockPosY = controllerPos.getY();
        int firstBlockPosZ = controllerPos.getZ() + relativeControllerPos.getFirst();
        for (int blockIndex : layout) {
            // Increase index
            index++;

            // Define position-related variables
            Pair<Integer, Integer> relativePosPair = getRelativePos(controller, index);
            int modZ = relativePosPair.getFirst();
            int modX = relativePosPair.getSecond();
            BlockPos curBlockPos = new BlockPos(firstBlockPosX + modX, blockPosY, firstBlockPosZ - modZ);

            // Check if block is correct
            if (level.getBlockState(curBlockPos).is(def.get(blockIndex))) {
                player.sendSystemMessage(Component.literal("Success").withStyle(ChatFormatting.GREEN));
                player.sendSystemMessage(Component.literal("| Block: "+level.getBlockState(curBlockPos)).withStyle(ChatFormatting.GRAY));
                player.sendSystemMessage(Component.literal("| Coordinates: "+curBlockPos).withStyle(ChatFormatting.GRAY));
            } else {
                player.sendSystemMessage(Component.literal("Failure").withStyle(ChatFormatting.RED));
                player.sendSystemMessage(Component.literal("| Block: " + level.getBlockState(curBlockPos)).withStyle(ChatFormatting.DARK_GRAY));
                player.sendSystemMessage(Component.literal("| Expected: " + def.get(blockIndex)).withStyle(ChatFormatting.DARK_GRAY));
                player.sendSystemMessage(Component.literal("| Coordinates: " + curBlockPos).withStyle(ChatFormatting.DARK_GRAY));
            }

        }
        IndustrialReforged.LOGGER.info(reverseDef.toString());
        player.sendSystemMessage(Component.literal("Controller index: "+
                reverseDef.get(controller.getMultiblock().getController())));
        player.sendSystemMessage(Component.literal("First block coordinates: x: " + firstBlockPosX + " z: " + firstBlockPosZ));
        player.sendSystemMessage(Component.literal("Controller rel pos: " + relativeControllerPos));
        return false;
    }

    // TODO: 10/21/2023 move this to the main for loop 
    public static Pair<Integer, Integer> getRelativePos(IMultiBlockController controller, int index) {
        List<Integer> layout = controller.getMultiblock().getLayout().get(0);
        int x = 0;
        int z = 0;
        int indexCopy = 0;
        for (int blockIndex : layout) {
            indexCopy++;
            if (indexCopy >= index) {
                return Pair.of(x, z);
            }
            if (x + 1 < controller.getMultiblock().getWidths().get(0)) {
                x++;
            } else {
                x = 0;
                z++;
            }
        }
        return null;
    }

    /**
     * @return x, z
     */
    public static Pair<Integer, Integer> getControllerRelativePos(IMultiBlockController controller) {
        Map<Block, Integer> reverseDef = Util.reverseMap(controller.getMultiblock().getDefinition());
        List<Integer> layout = controller.getMultiblock().getLayout().get(0);
        int x = 0;
        int z = 0;
        for (int blockIndex : layout) {
            if (blockIndex == reverseDef.get(controller.getMultiblock().getController())) {
                return Pair.of(x, z);
            }
            if (x + 1 < controller.getMultiblock().getWidths().get(0)) {
                x++;
            } else {
                x = 0;
                z++;
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
