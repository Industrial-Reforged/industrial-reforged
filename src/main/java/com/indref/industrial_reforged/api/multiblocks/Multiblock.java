package com.indref.industrial_reforged.api.multiblocks;

import com.indref.industrial_reforged.api.blockentities.multiblock.MultiblockEntity;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.IRRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.IntegerRange;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface Multiblock {
    /**
     * This method provides the controller block of your unformed multiblock.
     * Your multiblock needs at least one of these in its structure.
     * <br>
     * <br>
     * Example: {@link com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock#getUnformedController() BlastFurnaceMultiblock.getUnformedController()}
     * <br>
     * @return The controller block of your unformed multiblock
     */
    Block getUnformedController();

    /**
     * This method provides the controller block of your formed multiblock.
     * Your multiblock needs at least one of these in its structure.
     * <br>
     * <br>
     * Example: {@link com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock#getUnformedController() BlastFurnaceMultiblock.getFormedController()}
     * <br>
     * @return The controller block of your formed multiblock
     */
    Block getFormedController();

    /**
     * This method provides the layout of your unformed multiblock.
     * <br>
     * It consists of an array of multiblock layers. Each layer
     * is constructed with a method call.
     * <br>
     * For this, you can use {@link Multiblock#layer(int...)}
     * <br>
     * Each of these methods ask you to provide you a list of integers.
     * These integers represent the actual blocks used.
     * Nonetheless, you still need to provide the actual blocks using
     * the {@link Multiblock#getDefinition()} method.
     * This provides the minimum and maximum height for this multiblock.
     * <br>
     * Example: {@code IntegerRange.of(1, 3)}
     * <br>
     * <br>
     * Note: The first layer in this array also represents the bottom layer of the multiblock
     * <br>
     * <br>
     * Example: {@link com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock#getLayout() BlastFurnaceMultiblock.getLayout()}.
     * @return An array of multiblock layers that describes the layout of the multiblock
     */
    MultiblockLayer[] getLayout();

    /**
     * This method provides a definition map that can be used to look up
     * an integer key in {@link Multiblock#getLayout()} and will return a block.
     * <br>
     * <br>
     * The keyset of this map needs to include
     * every key that is used in {@link Multiblock#getLayout()}.
     * <br>
     * <br>
     * The values of this map need to contain the block for each
     * integer key. If you do not care about a block you can use {@code null}
     * instead of a value.
     * <br>
     * <br>
     * Example: {@link com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock#getDefinition() BlastFurnaceMultiblock.getDefintion()}
     * @return The integer to block map that provides the integer keys and their block values
     */
    Int2ObjectMap<Block> getDefinition();

    /**
     * This method provides the block entity type for the controller of your multiblock.
     * @return the blockentity type of your controllers blockentity
     */
    BlockEntityType<? extends MultiblockEntity> getMultiBlockEntityType();

    /**
     * This method provides a list of widths for every layer
     * of your multiblock.
     * <br>
     * <br>
     * This method has a default implementation meaning that
     * you do not have to override it, unless one of your
     * multiblock layers is not quadratic. (And it's width
     * can therefore not be determined by getting the
     * square root of the integer arrays length)
     * <br>
     * <br>
     * The size of this list needs to be {@link Multiblock#getMaxSize()}
     * and needs to contain the widths for every possible layer, this also
     * includes dynamic layers.
     *
     * @return a list of integer pairs where left is the x- and right is the z-width
     */
    default List<IntIntPair> getWidths() {
        List<IntIntPair> widths = new ArrayList<>(getMaxSize());
        for (MultiblockLayer layer : getLayout()) {
            if (layer.dynamic()) {
                for (int i = 0; i < layer.range().getMaximum(); i++) {
                    widths.add(layer.getWidths());
                }
            } else {
                widths.add(layer.getWidths());
            }
        }
        return widths;
    }

    /**
     * This method is used to form a block. It is called for that block and also when unforming the multi.
     * This is why this should only return the blockState, not perform any interactions on the level/player....
     * For interactions with the world/player..., use {@link Multiblock#afterFormBlock(Level, BlockPos, BlockPos, int, int, MultiblockData, Player)}
     * @param level Level of the multiblock, should only be used for reading things, not setting new things.
     * @param blockPos BlockPos of the block that is being formed
     * @param controllerPos BlockPos of this multiblocks controller
     * @param layerIndex index of the current layers block (array of integer)
     * @param layoutIndex index of the current multiblock layer (array of multiblock layer)
     * @param multiblockData Information about the unformed multiblock, like the layers of the concrete multiblock and the direction it is formed in.
     * @param player Player that is trying to form this multiblock. Note that there does not necessarily have to be a player that is responsible for forming the multiblock
     * @return Formed BlockState. This will replace the unformed block in the multiblock. Return {@code null} if you do not want to change the block.
     */
    @Nullable BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockData multiblockData, @Nullable Player player);

    /**
     * This method is called after the block is formed. It can be used to interact with the level/player...
     * as it is only called, when the multiblock is formed.
     * @param level Level of the multiblock
     * @param blockPos BlockPos of the block that is being formed
     * @param controllerPos BlockPos of this multiblocks controller
     * @param layerIndex index of the current layers block (array of integer)
     * @param layoutIndex index of the current multiblock layer (array of multiblock layer)
     * @param multiblockData Information about the unformed multiblock, like the layers of the concrete multiblock and the direction it is formed in.
     * @param player Player that is trying to form this multiblock. Note that there does not necessarily have to be a player that is responsible for forming the multiblock
     */
    default void afterFormBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockData multiblockData, @Nullable Player player) {
    }

    /**
     * This method is called after the block is unformed. It can be used to interact with the level/player...
     * as it is only called, when the multiblock is unformed.
     * @param level Level of the multiblock
     * @param direction Direction of the multiblock
     * @param blockPos BlockPos of the block that is being unformed
     * @param controllerPos BlockPos of this multiblocks controller
     * @param layerIndex index of the current layers block (array of integer)
     * @param layoutIndex index of the current multiblock layer (array of multiblock layer)
     * @param player Player that is trying to unform this multiblock. Note that there does not necessarily have to be a player that is responsible for unforming the multiblock
     */
    default void afterUnformBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, HorizontalDirection direction, @Nullable Player player) {
    }

    /**
     * This method determines whether the block at the specified position
     * is a formed part of this multiblock.
     * @param level Level of the multiblock
     * @param blockPos BlockPos that needs to be checked if it is formed.
     * @return Whether the block at this position is formed
     */
    boolean isFormed(Level level, BlockPos blockPos);

    /**
     * This method can make the direction of this multiblock fixed. This only works,
     * if the multiblock cannot be rotated, like the crucible or firebox.
     * Providing a fixed direction can improve performance while forming the multiblock
     * by a bit.
     * @return a horizontal direction, if the direction can be fixed.
     */
    default @Nullable HorizontalDirection getFixedDirection() {
        return null;
    }

    /**
     * This method provides the maximum possible
     * size for this multiblock.
     * @return the maximum possible size
     */
    default int getMaxSize() {
        int maxSize = 0;
        for (MultiblockLayer layer : getLayout()) {
            maxSize += layer.range().getMaximum();
        }
        return maxSize;
    }

    /**
     * Create a new layer for your multiblock
     * @param layer The block indices for your multiblock layer
     * @return the newly created layer
     */
    default MultiblockLayer layer(int... layer) {
        return new MultiblockLayer(false, IntegerRange.of(1, 1), layer);
    }

    Codec<Multiblock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("multiblock").forGetter(IRRegistries.MULTIBLOCK::getId)
    ).apply(instance, IRRegistries.MULTIBLOCK::byId));

    StreamCodec<RegistryFriendlyByteBuf, Multiblock> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            IRRegistries.MULTIBLOCK::getId,
            IRRegistries.MULTIBLOCK::byId
    );
}
