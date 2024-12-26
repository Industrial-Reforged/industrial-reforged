package com.indref.industrial_reforged.content.blocks.multiblocks.parts;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.items.tools.DisplayItem;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.content.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.networking.CrucibleTurnPayload;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.content.blockentities.multiblocks.part.CruciblePartBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.indref.industrial_reforged.content.multiblocks.CrucibleMultiblock.CRUCIBLE_WALL;

public class CruciblePartBlock extends BaseEntityBlock implements WrenchableBlock, DisplayBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final CrucibleTier tier;

    public CruciblePartBlock(Properties properties, CrucibleTier crucibleTier) {
        super(properties);
        this.tier = crucibleTier;
    }

    public CruciblePartBlock(Properties properties) {
        this(properties, null);
    }

    public CrucibleTier getTier() {
        return tier;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getPartShape(state, level, pos, context);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if (blockEntity instanceof CruciblePartBlockEntity cruciblePartBlockEntity) {
            BlockPos controllerPos = cruciblePartBlockEntity.getControllerPos();
            if (controllerPos != null) {
                IRMultiblocks.CRUCIBLE_CERAMIC.get().unform(controllerPos, level);
            }
        } else {
            IndustrialReforged.LOGGER.error("Failed to unform crucible, crucible wall blockentity corruption");
        }
        super.onRemove(blockState, level, blockPos, newState, p_60519_);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState p_60503_, Level level, BlockPos blockPos, Player player, BlockHitResult p_60508_) {
        CruciblePartBlockEntity blockEntity = (CruciblePartBlockEntity) level.getBlockEntity(blockPos);
        BlockPos controllerPos = blockEntity.getControllerPos();
        if (controllerPos != null) {
            CrucibleBlockEntity controllerBlockEntity = (CrucibleBlockEntity) level.getBlockEntity(controllerPos);
            if (controllerBlockEntity != null) {
                if (!player.isShiftKeyDown()) {
                    Utils.openMenu(player, controllerBlockEntity);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.useWithoutItem(p_60503_, level, blockPos, player, p_60508_);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.INVISIBLE;
    }

    protected @NotNull VoxelShape getPartShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CRUCIBLE_WALL)) {
            case EDGE_TOP -> switch (state.getValue(FACING)) {
                case NORTH -> VoxelShapes.MIDDLE_EDGE_NORTH;
                case EAST -> VoxelShapes.MIDDLE_EDGE_EAST;
                case SOUTH -> VoxelShapes.MIDDLE_EDGE_SOUTH;
                case WEST -> VoxelShapes.MIDDLE_EDGE_WEST;
                default -> super.getShape(state, level, pos, context);
            };
            case EDGE_BOTTOM -> switch (state.getValue(FACING)) {
                case NORTH -> VoxelShapes.BOTTOM_EDGE_NORTH;
                case EAST -> VoxelShapes.BOTTOM_EDGE_EAST;
                case SOUTH -> VoxelShapes.BOTTOM_EDGE_SOUTH;
                case WEST -> VoxelShapes.BOTTOM_EDGE_WEST;
                default -> super.getShape(state, level, pos, context);
            };
            case WALL_TOP -> switch (state.getValue(FACING)) {
                case NORTH -> VoxelShapes.MIDDLE_SIDE_NORTH;
                case EAST -> VoxelShapes.MIDDLE_SIDE_EAST;
                case SOUTH -> VoxelShapes.MIDDLE_SIDE_SOUTH;
                case WEST -> VoxelShapes.MIDDLE_SIDE_WEST;
                default -> super.getShape(state, level, pos, context);
            };
            case WALL_BOTTOM -> switch (state.getValue(FACING)) {
                case NORTH -> VoxelShapes.BOTTOM_SIDE_NORTH;
                case EAST -> VoxelShapes.BOTTOM_SIDE_EAST;
                case SOUTH -> VoxelShapes.BOTTOM_SIDE_SOUTH;
                case WEST -> VoxelShapes.BOTTOM_SIDE_WEST;
                default -> super.getShape(state, level, pos, context);
            };
            case FENCE -> super.getShape(state, level, pos, context);
        };
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return state.getValue(CRUCIBLE_WALL) == CrucibleMultiblock.WallStates.FENCE
                ? IRBlocks.IRON_FENCE.toStack()
                : IRBlocks.TERRACOTTA_BRICK.toStack();
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        BlockPos controllerPos = BlockUtils.getBE(level, pos, CruciblePartBlockEntity.class).getControllerPos();
        CrucibleBlockEntity be = BlockUtils.getBE(level, controllerPos, CrucibleBlockEntity.class);
        BlockPos blockPos = pos.subtract(neighborPos);
        Direction direction = Direction.fromDelta(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (direction != null) {
            boolean flag = level.hasSignal(neighborPos, direction);
            if (level.getBlockState(neighborPos).isSignalSource() && flag != be.isPowered()) {
                if (!be.isTurnedOver()) {
                    be.turn();
                    PacketDistributor.sendToPlayersTrackingChunk(((ServerLevel) level), new ChunkPos(pos), new CrucibleTurnPayload(controllerPos, flag, true));
                } else {
                    be.turnBack();
                    PacketDistributor.sendToPlayersTrackingChunk(((ServerLevel) level), new ChunkPos(pos), new CrucibleTurnPayload(controllerPos, flag, false));
                }
                be.setPowered(flag);
            }
        }
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        BlockPos controllerPos = BlockUtils.getBE(level, pos, CruciblePartBlockEntity.class).getControllerPos();

        if (state.getValue(CRUCIBLE_WALL) == CrucibleMultiblock.WallStates.WALL_BOTTOM) {
            BlockUtils.getBE(level, controllerPos, CrucibleBlockEntity.class).invalidateCapabilities();
        }

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(CRUCIBLE_WALL, FACING);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CruciblePartBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CruciblePartBlockEntity(blockPos, blockState);
    }

    @Override
    public Optional<Item> getDropItem() {
        return Optional.of(IRBlocks.TERRACOTTA_BRICK.get().asItem());
    }

    @Override
    public void displayOverlay(List<Component> displayText, Player player, Level level, ItemStack itemStack, BlockPos scannedBlockPos, BlockState scannedBlock) {
        BlockEntity blockEntity = level.getBlockEntity(scannedBlockPos);

        if (blockEntity instanceof CruciblePartBlockEntity fireboxPartBlockEntity) {
            BlockPos controllerPos = fireboxPartBlockEntity.getControllerPos();
            BlockEntity crucibleBlockEntity = level.getBlockEntity(controllerPos);
            if (crucibleBlockEntity instanceof CrucibleBlockEntity) {
                DisplayUtils.displayHeatInfo(displayText, crucibleBlockEntity.getBlockState(), crucibleBlockEntity.getBlockPos(), level);
            }
        }
    }

    @Override
    public List<ItemLike> getCompatibleItems() {
        return List.of(IRItems.THERMOMETER.get());
    }

    public static final class VoxelShapes {
        public static final VoxelShape TOP_NORTH = Shapes.or(Block.box(0, 0, 0, 16, 4, 3), Block.box(0, 0, 3, 3, 4, 16));
        public static final VoxelShape TOP_EAST = Shapes.or(Block.box(13, 0, 0, 16, 4, 16), Block.box(0, 0, 0, 13, 4, 3));
        public static final VoxelShape TOP_SOUTH = Shapes.or(Block.box(0, 0, 13, 16, 4, 16), Block.box(13, 0, 0, 16, 4, 13));
        public static final VoxelShape TOP_WEST = Shapes.or(Block.box(0, 0, 0, 3, 4, 16), Block.box(3, 0, 13, 16, 4, 16));

        public static final VoxelShape MIDDLE_EDGE_SOUTH = Shapes.or(Block.box(0, 0, 0, 16, 16, 4), Block.box(0, 0, 4, 4, 16, 16));
        public static final VoxelShape MIDDLE_EDGE_WEST = Shapes.or(Block.box(12, 0, 0, 16, 16, 16), Block.box(0, 0, 0, 12, 16, 4));
        public static final VoxelShape MIDDLE_EDGE_NORTH = Shapes.or(Block.box(0, 0, 12, 16, 16, 16), Block.box(12, 0, 0, 16, 16, 12));
        public static final VoxelShape MIDDLE_EDGE_EAST = Shapes.or(Block.box(0, 0, 0, 4, 16, 16), Block.box(4, 0, 12, 16, 16, 16));

        public static final VoxelShape BOTTOM_EDGE_BASE = Block.box(0, 8, 0, 16, 10, 16);

        public static final VoxelShape BOTTOM_EDGE_SOUTH = Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 8, 4, 4, 16, 16), Block.box(0, 10, 0, 16, 16, 4));
        public static final VoxelShape BOTTOM_EDGE_WEST = Shapes.or(BOTTOM_EDGE_BASE, Block.box(12, 8, 4, 16, 16, 16), Block.box(0, 10, 0, 16, 16, 4));
        public static final VoxelShape BOTTOM_EDGE_NORTH = Shapes.or(BOTTOM_EDGE_BASE, Block.box(12, 8, 0, 16, 16, 12), Block.box(0, 10, 12, 16, 16, 16));
        public static final VoxelShape BOTTOM_EDGE_EAST = Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 8, 0, 4, 16, 12), Block.box(0, 10, 12, 16, 16, 16));

        public static final VoxelShape MIDDLE_SIDE_WEST = Block.box(0, 0, 0, 16, 16, 4);
        public static final VoxelShape MIDDLE_SIDE_NORTH = Block.box(12, 0, 0, 16, 16, 16);
        public static final VoxelShape MIDDLE_SIDE_EAST = Block.box(0, 0, 12, 16, 16, 16);
        public static final VoxelShape MIDDLE_SIDE_SOUTH = Block.box(0, 0, 0, 4, 16, 16);

        public static final VoxelShape BOTTOM_SIDE_WEST = Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 8, 0, 16, 16, 4));
        public static final VoxelShape BOTTOM_SIDE_NORTH = Shapes.or(BOTTOM_EDGE_BASE, Block.box(12, 8, 0, 16, 16, 16));
        public static final VoxelShape BOTTOM_SIDE_EAST = Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 8, 12, 16, 16, 16));
        public static final VoxelShape BOTTOM_SIDE_SOUTH = Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 8, 0, 4, 16, 16));
    }
}
