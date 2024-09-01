package com.indref.industrial_reforged.registries.blocks.multiblocks.parts;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.part.CruciblePartBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock.CRUCIBLE_WALL;

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
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        // FIXME: Unforming not working
        if (blockEntity instanceof CruciblePartBlockEntity cruciblePartBlockEntity) {
            BlockPos controllerPos = cruciblePartBlockEntity.getControllerPos();
            if (controllerPos != null) {
                MultiblockHelper.unform(IRMultiblocks.CRUCIBLE_CERAMIC.get(), controllerPos, level);
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
            //Utils.openMenu(player, controllerBlockEntity);
            controllerBlockEntity.turn();
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CRUCIBLE_WALL)) {
//            case EDGE_TOP -> switch (state.getValue(FACING)) {
//                case NORTH -> VoxelShapes.TOP_NORTH;
//                case EAST -> VoxelShapes.TOP_EAST;
//                case SOUTH -> VoxelShapes.TOP_SOUTH;
//                case WEST -> VoxelShapes.TOP_WEST;
//                default -> super.getShape(state, level, pos, context);
//            };
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
        };
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
    public List<DisplayItem> getCompatibleItems() {
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

        public static final VoxelShape BOTTOM_EDGE_BASE = Block.box(0, 4, 0, 16, 6, 16);

        public static final VoxelShape BOTTOM_EDGE_SOUTH = Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 6, 4, 4, 16, 16), Block.box(0, 6, 0, 16, 16, 4));
        public static final VoxelShape BOTTOM_EDGE_WEST = Shapes.or(BOTTOM_EDGE_BASE, Block.box(12, 6, 4, 16, 16, 16), Block.box(0, 6, 0, 16, 16, 4));
        public static final VoxelShape BOTTOM_EDGE_NORTH = Shapes.or(BOTTOM_EDGE_BASE, Block.box(12, 6, 0, 16, 16, 12), Block.box(0, 6, 12, 16, 16, 16));
        public static final VoxelShape BOTTOM_EDGE_EAST = Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 6, 0, 4, 16, 12), Block.box(0, 6, 12, 16, 16, 16));

        public static final VoxelShape MIDDLE_SIDE_WEST = Block.box(0, 0, 0, 16, 16, 4);
        public static final VoxelShape MIDDLE_SIDE_NORTH = Block.box(12, 0, 0, 16, 16, 16);
        public static final VoxelShape MIDDLE_SIDE_EAST = Block.box(0, 0, 12, 16, 16, 16);
        public static final VoxelShape MIDDLE_SIDE_SOUTH = Block.box(0, 0, 0, 4, 16, 16);

        public static final VoxelShape BOTTOM_SIDE_WEST = Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 4, 0, 16, 16, 4));
        public static final VoxelShape BOTTOM_SIDE_NORTH = Shapes.or(BOTTOM_EDGE_BASE, Block.box(12, 4, 0, 16, 16, 16));
        public static final VoxelShape BOTTOM_SIDE_EAST= Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 4, 12, 16, 16, 16));
        public static final VoxelShape BOTTOM_SIDE_SOUTH = Shapes.or(BOTTOM_EDGE_BASE, Block.box(0, 4, 0, 4, 16, 16));
    }
}