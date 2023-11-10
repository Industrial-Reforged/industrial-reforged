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
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// TODO: 10/28/2023 Improve performance by reducing amount of for loops
public class MultiblockHelper {
    /**
     * Check if one of the multi's parts does not implement the {@link IMultiBlockPart} or {@link IMultiBlockController} interface.
     */
    public static boolean isOnlyParts(IMultiBlockController controller, Player player) {
        for (Block block : controller.getMultiblock().getDefinition().values()) {
            IndustrialReforged.LOGGER.info(block.toString());
            if (!(block instanceof IMultiBlockPart || block instanceof IMultiBlockController)) {
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

    /**
     * Converts a default minecraft direction to a multiblock direction.
     * Up and down will just return the default value (North)
     */
    public static MultiblockDirection convDirectionToMultiblockDirection(Direction direction) {
        return switch (direction) {
            case EAST -> MultiblockDirection.EAST;
            case SOUTH -> MultiblockDirection.SOUTH;
            case WEST -> MultiblockDirection.WEST;
            default -> MultiblockDirection.NORTH;
        };
    }

    /**
     * Check if all multiblock parts are placed correctly
     *
     * @param controller    multiblock's controller (multiblock to check)
     * @param controllerPos blockpos of the multiblock controller
     * @param level         level of the controller block
     * @param player        player that is trying to form the multi
     * @return first: isValid? second: Direction that multi is valid
     */
    public static Pair<Boolean, @Nullable MultiblockDirection> isValid(IMultiBlockController controller, BlockPos controllerPos, Level level, Player player) {
        IMultiblock multiblock = controller.getMultiblock();
        List<List<Integer>> layout = multiblock.getLayout();
        Map<Integer, Block> def = multiblock.getDefinition();
        Vec3i relativeControllerPos = getRelativeControllerPos(controller);
        MultiblockDirection direction = convDirectionToMultiblockDirection(player.getDirection());

        // Make player direction first entry of Set to prioritize
        Set<MultiblockDirection> directions = new HashSet<>();
        directions.add(direction);
        directions.addAll(List.of(MultiblockDirection.values()));

        // Indexing (Positions)
        int index = 0;
        int y = 0;

        // Debugging
        List<Boolean> multiblockIndexList = new ArrayList<>();

        // Check if controller exists
        if (relativeControllerPos == null) {
            throw new NullPointerException("Relative controller pos is not available. May be caused due to a multiblock layout without a controller");
        }

        Pair<List<Boolean>, MultiblockDirection> prioritizedDirectionLayout = Pair.of(List.of(), MultiblockDirection.NORTH);

        // Check if multi is valid
        // iterate through all possible directions
        for (MultiblockDirection mDirection : directions) {
            // Calculate block pos of the first block in the multi (multiblock.getLayout().get(0))
            Vec3i firstBlockPos = getFirstBlockPos(mDirection, controllerPos, relativeControllerPos);
            for (List<Integer> layer : layout) {
                for (int blockIndex : layer) {
                    // Increase index
                    index++;

                    // Define position-related variables
                    Vec3i relativePos = getRelativePos(controller, index, y);
                    BlockPos curBlockPos = getCurPos(firstBlockPos, relativePos, mDirection);

                    // Check if block is correct
                    if (level.getBlockState(curBlockPos).is(def.get(blockIndex))) {
                        multiblockIndexList.add(true);
                        // sendFailureMsg(player, level, curBlockPos, def, blockIndex);
                    } else {
                        multiblockIndexList.add(false);
                    }
                }
                index = 0;
                y++;
            }
            if (!multiblockIndexList.contains(false)) {
                return Pair.of(true, mDirection);
            }
            for (int i = multiblockIndexList.size() - 1; i >= 0; i--) {
                if (multiblockIndexList.get(i).equals(false)) {
                    multiblockIndexList.remove(i);
                }
            }
            if (multiblockIndexList.size() > prioritizedDirectionLayout.getFirst().size()) {
                prioritizedDirectionLayout = Pair.of(multiblockIndexList, mDirection);
            }
            multiblockIndexList = new ArrayList<>();
            y = 0;
        }

        MultiblockDirection prioritizedDirection = prioritizedDirectionLayout.getSecond();

        Vec3i firstBlockPos = getFirstBlockPos(prioritizedDirection, controllerPos, relativeControllerPos);
        for (List<Integer> layer : layout) {
            for (int blockIndex : layer) {
                // Increase index
                index++;

                // Define position-related variables
                Vec3i relativePos = getRelativePos(controller, index, y);

                BlockPos curBlockPos = getCurPos(firstBlockPos, relativePos, prioritizedDirection);

                // Check if block is correct
                if (!level.getBlockState(curBlockPos).is(def.get(blockIndex))) {
                    sendFailureMsg(player, level, curBlockPos, def, blockIndex);
                    return Pair.of(false, null);
                }
            }
            index = 0;
            y++;
        }

        return Pair.of(false, null);
    }

    public static BlockPos getCurPos(Vec3i firstPos, Vec3i relativePos, MultiblockDirection direction) {
        int firstBlockPosX = firstPos.getX();
        int firstBlockPosY = firstPos.getY();
        int firstBlockPosZ = firstPos.getZ();
        int modZ = relativePos.getZ();
        int y = relativePos.getY();
        int modX = relativePos.getX();
        return switch (direction) {
            case NORTH -> new BlockPos(firstBlockPosX + modX, firstBlockPosY + y, firstBlockPosZ + modZ);
            case EAST -> new BlockPos(firstBlockPosX - modZ, firstBlockPosY + y, firstBlockPosZ + modX);
            case SOUTH -> new BlockPos(firstBlockPosX - modX, firstBlockPosY + y, firstBlockPosZ - modZ);
            case WEST -> new BlockPos(firstBlockPosX + modZ, firstBlockPosY + y, firstBlockPosZ - modX);
        };
    }

    private static Vec3i getFirstBlockPos(MultiblockDirection direction, BlockPos controllerPos, Vec3i relativeControllerPos) {
        int firstBlockPosX = switch (direction) {
            case NORTH -> controllerPos.getX() - relativeControllerPos.getX();
            case EAST -> controllerPos.getX() + relativeControllerPos.getZ();
            case SOUTH -> controllerPos.getX() + relativeControllerPos.getX();
            case WEST -> controllerPos.getX() - relativeControllerPos.getZ();
        };
        int firstBlockPosY = controllerPos.getY() - relativeControllerPos.getY();
        int firstBlockPosZ = switch (direction) {
            case NORTH -> controllerPos.getZ() - relativeControllerPos.getZ();
            case EAST -> controllerPos.getZ() - relativeControllerPos.getX();
            case SOUTH -> controllerPos.getZ() + relativeControllerPos.getZ();
            case WEST -> controllerPos.getZ() + relativeControllerPos.getX();
        };
        return new Vec3i(firstBlockPosX, firstBlockPosY, firstBlockPosZ);
    }

    // TODO: 10/21/2023 move this to the main for loop
    public static Vec3i getRelativePos(IMultiBlockController controller, int index, int yLevel) {
        List<Integer> layout = controller.getMultiblock().getLayout().get(yLevel);
        int x = 0;
        int z = 0;
        int indexCopy = 0;
        int width = controller.getMultiblock().getWidths().get(yLevel).getFirst();
        for (int ignored : layout) {
            indexCopy++;
            if (indexCopy >= index) {
                return new Vec3i(x, yLevel, z);
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
    public static Vec3i getRelativeControllerPos(IMultiBlockController controller) {
        Map<Block, Integer> reverseDef = Util.reverseMap(controller.getMultiblock().getDefinition());
        List<List<Integer>> layout = controller.getMultiblock().getLayout();
        int x = 0;
        int y = 0;
        int z = 0;
        for (List<Integer> layer : layout) {
            int width = controller.getMultiblock().getWidths().get(y).getFirst();
            for (int blockIndex : layer) {
                if (blockIndex == reverseDef.get(controller.getMultiblock().getController())) {
                    return new Vec3i(x, y, z);
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
            formBlocks(controller, direction, controllerPos, level);
        }
    }

    private static void formBlocks(IMultiBlockController controller, MultiblockDirection direction, BlockPos controllerPos, Level level) {
        Vec3i relativeControllerPos = getRelativeControllerPos(controller);
        // Calculate block pos of the first block in the multi (multiblock.getLayout().get(0))
        Vec3i firstBlockPos = getFirstBlockPos(direction, controllerPos, relativeControllerPos);
        List<List<Integer>> layout = controller.getMultiblock().getLayout();

        int index = 0;
        int yIndex = 0;
        for (List<Integer> layer : layout) {
            for (int ignored : layer) {
                // Increase index
                index++;

                // Define position-related variables
                Vec3i relativePos = getRelativePos(controller, index, yIndex);
                BlockPos curBlockPos = getCurPos(firstBlockPos, relativePos, direction);

                controller.getMultiblock().formBlock(level, curBlockPos, index - 1, yIndex);
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
