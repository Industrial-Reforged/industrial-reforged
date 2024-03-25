package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.api.multiblocks.SavesControllerPos;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class MultiblockUtils {

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
     * @param multiblock    multiblock's controller (multiblock to check)
     * @param controllerPos blockpos of the multiblock controller
     * @param level         level of the controller block
     * @param player        player that is trying to form the multi
     * @return first: isValid? second: Direction that multi is valid
     */
    public static Pair<Boolean, @Nullable MultiblockDirection> isValid(Multiblock multiblock, BlockPos controllerPos, Level level, Player player) {
        List<List<Integer>> layout = multiblock.getLayout();
        Map<Integer, Block> def = multiblock.getDefinition();
        Vec3i relativeControllerPos = getRelativeControllerPos(multiblock);
        MultiblockDirection direction = convDirectionToMultiblockDirection(player.getDirection());

        // Make player direction first entry of Set to prioritize
        Set<MultiblockDirection> directions = new HashSet<>();
        directions.add(direction);
        directions.addAll(List.of(MultiblockDirection.values()));

        // Indexing (Positions)
        int y = 0;

        // Debugging
        List<Boolean> multiblockIndexList = new ArrayList<>();

        // Check if controller exists
        if (relativeControllerPos == null) {
            throw new NullPointerException("Relative controller pos is not available. May be caused due to a multiblock layout without a controller");
        }

        Pair<List<Boolean>, MultiblockDirection> prioritizedDirectionLayout = Pair.of(List.of(), MultiblockDirection.NORTH);
        // Direction, blockPos, blockDefinitionIndex
        Map<MultiblockDirection, Pair<BlockPos, Integer>> firstMissingBlockPoses = new HashMap<>();

        // Check if multi is valid
        // iterate through all possible directions
        for (MultiblockDirection mDirection : directions) {
            // Calculate block pos of the first block in the multi (multiblock.getLayout().get(0))
            BlockPos firstBlockPos = getFirstBlockPos(mDirection, controllerPos, relativeControllerPos);
            // Iterate over layers (Y)
            for (List<Integer> layer : layout) {
                // Iterate over blocks in a layer (X, Z)
                int x = 0;
                int z = 0;
                int width = multiblock.getWidths().get(y).getFirst();
                for (int blockIndex : layer) {
                    // Increase index

                    // Define position-related variables
                    BlockPos curBlockPos = getCurPos(firstBlockPos, new Vec3i(x, y, z), mDirection);

                    // Check if block is correct
                    if ((def.get(blockIndex) != null && level.getBlockState(curBlockPos).is(def.get(blockIndex)) || def.get(blockIndex) == null)) {
                        multiblockIndexList.add(true);
                    } else {
                        firstMissingBlockPoses.putIfAbsent(mDirection, Pair.of(curBlockPos, blockIndex));
                        multiblockIndexList.add(false);
                    }
                    if (x + 1 < width) {
                        x++;
                    } else {
                        x = 0;
                        z++;
                    }
                }
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

        sendFailureMsg(player, level, firstMissingBlockPoses.get(prioritizedDirection).getFirst(), def, firstMissingBlockPoses.get(prioritizedDirection).getSecond());

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

    private static BlockPos getFirstBlockPos(MultiblockDirection direction, BlockPos controllerPos, Vec3i relativeControllerPos) {
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
        return new BlockPos(firstBlockPosX, firstBlockPosY, firstBlockPosZ);
    }

    /**
     * @return x, y, z
     */
    @Nullable
    public static Vec3i getRelativeControllerPos(Multiblock multiblock) {
        Map<Block, Integer> reverseDef = Utils.reverseMap(multiblock.getDefinition());
        List<List<Integer>> layout = multiblock.getLayout();
        int y = 0;
        for (List<Integer> layer : layout) {
            int x = 0;
            int z = 0;
            int width = multiblock.getWidths().get(y).getFirst();
            for (int blockIndex : layer) {
                if (blockIndex == reverseDef.get(multiblock.getController())) {
                    return new Vec3i(x, y, z);
                }
                if (x + 1 < width) {
                    x++;
                } else {
                    x = 0;
                    z++;
                }
            }
            y++;
        }
        return null;
    }

    private static void sendFailureMsg(Player player, Level level, BlockPos curBlockPos, Map<Integer, Block> def, int blockIndex) {
        player.sendSystemMessage(Component.translatable("multiblock.info.failed_to_construct").withStyle(ChatFormatting.RED));
        player.sendSystemMessage(Component.literal("| ")
                .withStyle(ChatFormatting.DARK_GRAY)
                .append(Component.translatable("multiblock.info.actual_block")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(": ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(level.getBlockState(curBlockPos).getBlock().getName().getString())
                        .withStyle(ChatFormatting.DARK_GRAY))
        );
        player.sendSystemMessage((Component.literal("| ")
                .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.translatable("multiblock.info.expected_block")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(": ")
                        .withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(def.get(blockIndex).getName().getString())
                        .withStyle(ChatFormatting.DARK_GRAY))
        );
        player.sendSystemMessage(
                Component.literal("| ")
                        .withStyle(ChatFormatting.DARK_GRAY)
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

    public static void form(Multiblock multiblock, BlockPos controllerPos, Level level, Player player) {
        Pair<Boolean, MultiblockDirection> valid = isValid(multiblock, controllerPos, level, player);
        MultiblockDirection direction = valid.getSecond();
        if (multiblock.getFixedDirection() != null) {
            direction = multiblock.getFixedDirection();
        }
        if (valid.getFirst()) {
            formBlocks(multiblock, direction, controllerPos, level);
        }
    }

    public static void unform(Multiblock multiblock, BlockPos controllerPos, Level level) {
        if (multiblock.getFixedDirection() != null) {
            for (MultiblockDirection direction1 : MultiblockDirection.values()) {
                unformBlocks(multiblock, direction1, controllerPos, level);
            }
        }
    }

    private static void unformBlocks(Multiblock multiblock, MultiblockDirection direction, BlockPos controllerPos, Level level) {
        Vec3i relativeControllerPos = getRelativeControllerPos(multiblock);
        // Calculate block pos of the first block in the multi (multiblock.getLayout().get(0))
        BlockPos firstBlockPos = getFirstBlockPos(direction, controllerPos, relativeControllerPos);
        List<List<Integer>> layout = multiblock.getLayout();
        Map<Integer, Block> definition = multiblock.getDefinition();
        IndustrialReforged.LOGGER.debug("first: {}", firstBlockPos);
        int yIndex = 0;
        int xIndex = 0;
        IndustrialReforged.LOGGER.debug("TEST2");
        for (List<Integer> layer : layout) {
            // relative position
            int x = 0;
            // multiblock index
            int width = multiblock.getWidths().get(yIndex).getFirst();
            int z = 0;
            for (int blockIndex : layer) {
                Block definedBlock = definition.get(blockIndex);
                xIndex++;
                // Increase index
                BlockPos curBlockPos = getCurPos(firstBlockPos, new Vec3i(x, yIndex, z), direction);

                BlockState blockState = level.getBlockState(curBlockPos);
                BlockState expectedState = multiblock.formBlock(level, direction, curBlockPos, controllerPos, xIndex-1, yIndex);
                if (expectedState != null && blockState.is(expectedState.getBlock()) && multiblock.isFormed(level, curBlockPos, controllerPos)) {
                    multiblock.unformBlock(level, curBlockPos, controllerPos);
                    MultiblockUtils.setAndUpdate(level, curBlockPos, definedBlock.defaultBlockState());
                }

                if (x + 1 < width) {
                    x++;
                } else {
                    x = 0;
                    z++;
                }
            }
            xIndex = 0;
            yIndex++;
        }
    }

    private static void formBlocks(Multiblock multiblock, MultiblockDirection direction, BlockPos controllerPos, Level level) {
        Vec3i relativeControllerPos = getRelativeControllerPos(multiblock);
        // Calculate block pos of the first block in the multi (multiblock.getLayout().get(0))
        BlockPos firstBlockPos = getFirstBlockPos(direction, controllerPos, relativeControllerPos);
        List<List<Integer>> layout = multiblock.getLayout();
        Map<Integer, Block> def = multiblock.getDefinition();

        int index = 0;
        int yIndex = 0;
        for (List<Integer> layer : layout) {
            int x = 0;
            int width = multiblock.getWidths().get(yIndex).getFirst();
            int z = 0;
            for (int blockIndex : layer) {
                // Increase index
                index++;

                BlockPos curBlockPos = getCurPos(firstBlockPos, new Vec3i(x, yIndex, z), direction);

                if (def.get(blockIndex) != null) {
                    BlockState newState = multiblock.formBlock(level, direction, curBlockPos, controllerPos, index - 1, yIndex);
                    if (newState != null) level.setBlockAndUpdate(curBlockPos, newState);
                    BlockEntity blockEntity = level.getBlockEntity(curBlockPos);
                    if (blockEntity instanceof SavesControllerPos savesControllerPosBE) {
                        savesControllerPosBE.setControllerPos(controllerPos);
                    }
                }

                if (x + 1 < width) {
                    x++;
                } else {
                    x = 0;
                    z++;
                }
            }
            index = 0;
            yIndex++;
        }
    }

    public static void setAndUpdate(Level level, BlockPos blockPos, BlockState newState) {
        level.setBlockAndUpdate(blockPos, newState);
    }

    public static List<List<Integer>> singleBlockMultiblock(List<Integer> yLayout) {
        List<List<Integer>> toReturn = new ArrayList<>();
        for (int yLayer : yLayout) {
            toReturn.add(List.of(yLayer));
        }
        return toReturn;
    }
}
