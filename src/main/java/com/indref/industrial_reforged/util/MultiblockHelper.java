package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.IMultiBlockPart;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import com.indref.industrial_reforged.util.Util.XZCoordinates;
import com.indref.industrial_reforged.util.Util.Coordinates;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// TODO: 10/28/2023 Improve performance by reducing amount of for loops
public class MultiblockHelper {
    public static boolean isOnlyParts(IMultiBlockController controller, Player player) {
        IndustrialReforged.LOGGER.info("Checking parts");
        for (var block : controller.getMultiblock().getDefinition().values()) {
            IndustrialReforged.LOGGER.info(block.toString());
            if (!(block instanceof IMultiBlockPart || block instanceof IMultiBlockController)) {
                // TODO: 10/24/2023 Consider making this an error message that prevents the game from starting?
                player.sendSystemMessage(
                        Component.literal("ERROR: Report this to the creator/maintainer of the mod. " +
                                        "One of the multiblock's blocks does not implement the IMultiblockPart interface")
                                .withStyle(ChatFormatting.RED)
                );
                return false;
            }
        }
        return true;
    }

    public static MultiblockDirection convDirectionToMultiblockDirection(Direction direction) {
        return switch (direction) {
            case EAST -> MultiblockDirection.EAST;
            case SOUTH -> MultiblockDirection.SOUTH;
            case WEST -> MultiblockDirection.WEST;
            default -> MultiblockDirection.NORTH;
        };
    }

    public static Pair<Boolean, @Nullable MultiblockDirection> isValid(IMultiBlockController controller, BlockPos controllerPos, Level level, Player player) {
        MultiblockDirection direction = convDirectionToMultiblockDirection(player.getDirection());
        Set<MultiblockDirection> directions = new HashSet<>();
        directions.add(direction);
        directions.addAll(List.of(MultiblockDirection.values()));

        IMultiblock multiblock = controller.getMultiblock();
        List<List<Integer>> layout = multiblock.getLayout();
        Coordinates relativeControllerPos = getRelativeControllerPos(controller);

        // Block definition of the multi
        Map<Integer, Block> def = multiblock.getDefinition();
        Map<Block, Integer> reverseDef = Util.reverseMap(multiblock.getDefinition());

        // Indexing (Positions)
        int index = 0;
        int yIndex = 0;

        // Debugging
        List<Boolean> testBlockIndexList = new ArrayList<>();

        // Check if controller exists
        if (relativeControllerPos == null) {
            throw new NullPointerException("Relative controller pos is not available. May be caused due to a multiblock layout without a controller");
        }

        Pair<List<Boolean>, MultiblockDirection> prioritizedDirectionLayout = Pair.of(List.of(), MultiblockDirection.NORTH);

        // Check if multi is valid
        // iterate through all possible directions
        for (MultiblockDirection mDirection : directions) {
            // Calculate block pos of the first block in the multi (multiblock.getLayout().get(0))
            Coordinates firstBlockPos = getFirstBlockPos(mDirection, controllerPos, relativeControllerPos);
            int firstBlockPosX = firstBlockPos.getFirst();
            int firstBlockPosY = firstBlockPos.getSecond();
            int firstBlockPosZ = firstBlockPos.getThird();
            for (List<Integer> layer : layout) {
                for (int blockIndex : layer) {
                    // Increase index
                    index++;

                    // Define position-related variables
                    XZCoordinates relativePos = getRelativePos(controller, index, yIndex);
                    int modZ = relativePos.getSecond();
                    int modX = relativePos.getFirst();
                    BlockPos curBlockPos = switch (mDirection) {
                        case NORTH ->
                                new BlockPos(firstBlockPosX + modX, firstBlockPosY + yIndex, firstBlockPosZ + modZ);
                        case EAST ->
                                new BlockPos(firstBlockPosX - modZ, firstBlockPosY + yIndex, firstBlockPosZ + modX);
                        case SOUTH ->
                                new BlockPos(firstBlockPosX - modX, firstBlockPosY + yIndex, firstBlockPosZ - modZ);
                        case WEST ->
                                new BlockPos(firstBlockPosX + modZ, firstBlockPosY + yIndex, firstBlockPosZ - modX);
                    };

                    // Check if block is correct
                    if (level.getBlockState(curBlockPos).is(def.get(blockIndex))) {
                        testBlockIndexList.add(true);
                        // sendFailureMsg(player, level, curBlockPos, def, blockIndex);
                    } else {
                        testBlockIndexList.add(false);
                    }
                }
                index = 0;
                yIndex++;
            }
            if (!testBlockIndexList.contains(false)) {
                return Pair.of(true, mDirection);
            }
            for (int i = testBlockIndexList.size() - 1; i >= 0; i--) {
                if (testBlockIndexList.get(i).equals(false)) {
                    testBlockIndexList.remove(i);
                }
            }
            if (testBlockIndexList.size() > prioritizedDirectionLayout.getFirst().size()) {
                prioritizedDirectionLayout = Pair.of(testBlockIndexList, mDirection);
            }
            testBlockIndexList = new ArrayList<>();
            yIndex = 0;
        }

        MultiblockDirection prioritizedDirection = prioritizedDirectionLayout.getSecond();

        Coordinates firstBlockPos = getFirstBlockPos(prioritizedDirection, controllerPos, relativeControllerPos);
        int firstBlockPosX = firstBlockPos.getFirst();
        int firstBlockPosY = firstBlockPos.getSecond();
        int firstBlockPosZ = firstBlockPos.getThird();
        for (List<Integer> layer : layout) {
            for (int blockIndex : layer) {
                // Increase index
                index++;

                // Define position-related variables
                XZCoordinates relativePos = getRelativePos(controller, index, yIndex);
                int modZ = relativePos.getSecond();
                int modX = relativePos.getFirst();
                BlockPos curBlockPos = switch (prioritizedDirection) {
                    case NORTH -> new BlockPos(firstBlockPosX + modX, firstBlockPosY + yIndex, firstBlockPosZ + modZ);
                    case EAST -> new BlockPos(firstBlockPosX - modZ, firstBlockPosY + yIndex, firstBlockPosZ + modX);
                    case SOUTH -> new BlockPos(firstBlockPosX - modX, firstBlockPosY + yIndex, firstBlockPosZ - modZ);
                    case WEST -> new BlockPos(firstBlockPosX + modZ, firstBlockPosY + yIndex, firstBlockPosZ - modX);
                };

                // Check if block is correct
                if (!level.getBlockState(curBlockPos).is(def.get(blockIndex))) {
                    sendFailureMsg(player, level, curBlockPos, def, blockIndex);
                    return Pair.of(false, null);
                }
            }
            index = 0;
            yIndex++;
        }

        return Pair.of(false, null);
    }

    private static Coordinates getFirstBlockPos(MultiblockDirection direction, BlockPos controllerPos, Coordinates relativeControllerPos) {
        int firstBlockPosX = switch (direction) {
            case NORTH -> controllerPos.getX() - relativeControllerPos.getFirst();
            case EAST -> controllerPos.getX() + relativeControllerPos.getThird();
            case SOUTH -> controllerPos.getX() + relativeControllerPos.getFirst();
            case WEST -> controllerPos.getX() - relativeControllerPos.getThird();
        };
        int firstBlockPosY = controllerPos.getY() - relativeControllerPos.getSecond();
        int firstBlockPosZ = switch (direction) {
            case NORTH -> controllerPos.getZ() - relativeControllerPos.getThird();
            case EAST -> controllerPos.getZ() - relativeControllerPos.getFirst();
            case SOUTH -> controllerPos.getZ() + relativeControllerPos.getThird();
            case WEST -> controllerPos.getZ() + relativeControllerPos.getFirst();
        };
        return Coordinates.of(firstBlockPosX, firstBlockPosY, firstBlockPosZ);
    }

    // TODO: 10/21/2023 move this to the main for loop 
    public static XZCoordinates getRelativePos(IMultiBlockController controller, int index, int yLevel) {
        List<Integer> layout = controller.getMultiblock().getLayout().get(yLevel);
        int x = 0;
        int z = 0;
        int indexCopy = 0;
        int width = controller.getMultiblock().getWidths().get(yLevel).getFirst();
        for (int ignored : layout) {
            indexCopy++;
            if (indexCopy >= index) {
                return XZCoordinates.of(x, z);
            }
            if (x + 1 < width) {
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
    public static Coordinates getRelativeControllerPos(IMultiBlockController controller) {
        Map<Block, Integer> reverseDef = Util.reverseMap(controller.getMultiblock().getDefinition());
        List<List<Integer>> layout = controller.getMultiblock().getLayout();
        int x = 0;
        int y = 0;
        int z = 0;
        for (List<Integer> layer : layout) {
            int width = controller.getMultiblock().getWidths().get(y).getFirst();
            for (int blockIndex : layer) {
                if (blockIndex == reverseDef.get(controller.getMultiblock().getController())) {
                    return Coordinates.of(x, y, z);
                }
                if (x + 1 < width) {
                    x++;
                } else {
                    x = 0;
                    z++;
                }
            }
            x = 0;
            z = 0;
            y++;
        }
        return null;
    }

    private static void sendFailureMsg(Player player, Level level, BlockPos curBlockPos, Map<Integer, Block> def, int blockIndex) {
        player.sendSystemMessage(Component.translatable("multiblock.info.failed_to_construct").withStyle(ChatFormatting.RED));
        player.sendSystemMessage(MutableComponent.create(ComponentContents.EMPTY)
                .append(Component.literal("| ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.translatable("multiblock.info.actual_block")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(": ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(level.getBlockState(curBlockPos).getBlock().getName().getString())
                        .withStyle(ChatFormatting.DARK_GRAY))
        );
        player.sendSystemMessage(MutableComponent.create(ComponentContents.EMPTY)
                .append(Component.literal("| ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.translatable("multiblock.info.expected_block")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(": ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(def.get(blockIndex).getName().getString())
                        .withStyle(ChatFormatting.DARK_GRAY))
        );
        player.sendSystemMessage(MutableComponent.create(ComponentContents.EMPTY)
                .append(Component.literal("| ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.translatable("multiblock.info.block_pos")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(": ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(String.valueOf(curBlockPos.getX()))
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(", ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(String.valueOf(curBlockPos.getY()))
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(", ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(String.valueOf(curBlockPos.getZ()))
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(", ")
                        .withStyle(ChatFormatting.DARK_GRAY))
        );
    }

    public static void form(IMultiBlockController controller, BlockPos controllerPos, Level level, Player player) {
        Pair<Boolean, MultiblockDirection> valid = isValid(controller, controllerPos, level, player);
        MultiblockDirection direction = valid.getSecond();
        if (controller.getMultiblock().getFixedDirection() != null) {
            direction = controller.getMultiblock().getFixedDirection();
        }
        if (isOnlyParts(controller, player) && valid.getFirst()) {
            formBlocks(controller, direction, controllerPos, level, player);
            player.sendSystemMessage(Component.literal("Simulating forming").withStyle(ChatFormatting.GREEN));
        }
    }

    private static void formBlocks(IMultiBlockController controller, MultiblockDirection direction, BlockPos controllerPos, Level level, Player player) {
        Coordinates relativeControllerPos = getRelativeControllerPos(controller);
        // Calculate block pos of the first block in the multi (multiblock.getLayout().get(0))
        Coordinates firstBlockPos = getFirstBlockPos(direction, controllerPos, relativeControllerPos);
        int firstBlockPosX = firstBlockPos.getFirst();
        int firstBlockPosY = firstBlockPos.getSecond();
        int firstBlockPosZ = firstBlockPos.getThird();
        List<List<Integer>> layout = controller.getMultiblock().getLayout();

        int index = 0;
        int yIndex = 0;
        for (List<Integer> layer : layout) {
            for (int blockIndex : layer) {
                // Increase index
                index++;

                // Define position-related variables
                XZCoordinates relativePos = getRelativePos(controller, index, yIndex);
                int modZ = relativePos.getSecond();
                int modX = relativePos.getFirst();
                BlockPos curBlockPos = switch (direction) {
                    case NORTH -> new BlockPos(firstBlockPosX + modX, firstBlockPosY + yIndex, firstBlockPosZ + modZ);
                    case EAST -> new BlockPos(firstBlockPosX - modZ, firstBlockPosY + yIndex, firstBlockPosZ + modX);
                    case SOUTH -> new BlockPos(firstBlockPosX - modX, firstBlockPosY + yIndex, firstBlockPosZ - modZ);
                    case WEST -> new BlockPos(firstBlockPosX + modZ, firstBlockPosY + yIndex, firstBlockPosZ - modX);
                };

                controller.getMultiblock().formBlock(level, curBlockPos, index-1, yIndex);
                player.sendSystemMessage(Component.literal("index: "+(index-1)+", yIndex: "+yIndex+", blockpos: "+curBlockPos+", modX: "+modX));
                player.sendSystemMessage(Component.literal("Direction: "+direction));
            }
            index = 0;
            yIndex++;
        }
    }

    public static void setAndUpdate(Level level, BlockPos blockPos, BlockState oldState, BlockState newState) {
        level.setBlock(blockPos, newState, 2);
        level.sendBlockUpdated(blockPos, oldState, newState, 11);
    }
}
