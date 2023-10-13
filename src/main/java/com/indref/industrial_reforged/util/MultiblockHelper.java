package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblock.IMultiblock;
import com.indref.industrial_reforged.api.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class MultiblockHelper {
    private final IMultiblockController multiblockController;
    public MultiblockHelper(IMultiblockController multiblockController) {
        this.multiblockController = multiblockController;
    }

    // TODO: 10/13/2023 player is only used for Debugging
    public boolean isValid(BlockPos controllerBlockPos, Player player) {
        IMultiblock multiblock = multiblockController.getMultiblock();

        int controllerID = getIdByBlock(multiblock.getController());
        String multiblockName = multiblock.getController().getName().getString();

        boolean foundController = false;
        // x-axis
        int weight = 0;
        // y-axis
        int height = multiblock.getLayout().size();
        // z-axis
        int depth = 0;
        for (int layerIndex = 0; layerIndex < multiblock.getLayout().size(); layerIndex++) {
            if (multiblock.getLayout().get(layerIndex).contains(controllerID)) {
                if (!foundController) {
                    for (int blockIndex = 0; blockIndex < multiblock.getLayout().get(layerIndex).size(); blockIndex++) {
                        if (multiblock.getLayout().get(layerIndex).get(blockIndex) == controllerID) {
                            if (!foundController) {
                                foundController = true;
                                player.sendSystemMessage(Component.literal("Found controller at layer: " + layerIndex + ", blockIndex: " + blockIndex));
                            } else {
                                IndustrialReforged.LOGGER.error("Multiblock: " + multiblockName + " has more than one controller");
                            }
                        } else {
                            for (int defIndex = 0; defIndex < multiblock.getLayout().size(); defIndex++) {
                                if (multiblock.getLayout().get(layerIndex).get(blockIndex).equals(defIndex)) {
                                    player.sendSystemMessage(
                                            Component.literal("Found matching block at layer: " + layerIndex + ", block:" + blockIndex + ", name: " +
                                                    multiblock.getDefinition().get(defIndex)));
                                }
                            }
                        }
                    }
                }
            }
        }
        Block neighborBelow = player.level().getBlockState(new BlockPos(controllerBlockPos.getX(), controllerBlockPos.getY()-1, controllerBlockPos.getZ())).getBlock();
        player.sendSystemMessage(Component.literal("x: "+controllerBlockPos.getX()+", y: "+controllerBlockPos.getY()+", z: "+controllerBlockPos.getZ()));
        player.sendSystemMessage(Component.literal("neighborBelow: "+neighborBelow.getName().getString()));
        player.sendSystemMessage(Component.literal("widths: "+multiblock.getWidths().get(0)));
        return false;
    }

    public int getIdByBlock(Block block) {
        IMultiblock multiblock = multiblockController.getMultiblock();

        for (Map.Entry<Integer, Block> entry : multiblock.getDefinition().entrySet()) {
            Integer k = entry.getKey();
            Block v = entry.getValue();
            if (v == block) {
                return k;
            }
        }
        return -1;
    }

    public void form() {
        IMultiblock multiblock = multiblockController.getMultiblock();
        IndustrialReforged.LOGGER.info("Attempting to form multiblock: "+multiblock);
    }
}
