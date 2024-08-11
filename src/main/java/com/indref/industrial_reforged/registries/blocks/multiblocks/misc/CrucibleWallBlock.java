package com.indref.industrial_reforged.registries.blocks.multiblocks.misc;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.misc.CrucibleWallBlockEntity;
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

public class CrucibleWallBlock extends BaseEntityBlock implements WrenchableBlock, DisplayBlock {
    public static final VoxelShape SHAPE_TOP = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    public static final VoxelShape SHAPE_BOTTOM = Block.box(0.0, 4.0, 0.0, 16.0, 16.0, 16.0);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final CrucibleTier tier;

    public CrucibleWallBlock(Properties properties, CrucibleTier crucibleTier) {
        super(properties);
        this.tier = crucibleTier;
    }

    public CrucibleWallBlock(Properties properties) {
        this(properties, null);
    }

    public CrucibleTier getTier() {
        return tier;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        // FIXME: Unforming not working
        if (blockEntity instanceof CrucibleWallBlockEntity crucibleWallBlockEntity) {
            crucibleWallBlockEntity.getControllerPos().ifPresent(pos -> MultiblockHelper.unform(IRMultiblocks.CRUCIBLE_CERAMIC.get(), pos, level));
        } else {
            IndustrialReforged.LOGGER.error("Failed to unform crucible, crucible wall blockentity corruption");
        }
        super.onRemove(blockState, level, blockPos, newState, p_60519_);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState p_60503_, Level level, BlockPos blockPos, Player player, BlockHitResult p_60508_) {
        CrucibleWallBlockEntity blockEntity = (CrucibleWallBlockEntity) level.getBlockEntity(blockPos);
        blockEntity.getControllerPos().ifPresent(pos -> {
            CrucibleBlockEntity controllerBlockEntity = (CrucibleBlockEntity) level.getBlockEntity(pos);
            Utils.openMenu(player, controllerBlockEntity);
        });
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
                case NORTH -> VoxelShapes.MIDDLE_NORTH;
                case EAST -> VoxelShapes.MIDDLE_EAST;
                case SOUTH -> VoxelShapes.MIDDLE_SOUTH;
                case WEST -> VoxelShapes.MIDDLE_WEST;
                default -> super.getShape(state, level, pos, context);
            };
            case EDGE_BOTTOM -> switch (state.getValue(FACING)) {
                case NORTH -> VoxelShapes.BOTTOM_NORTH;
                case EAST -> VoxelShapes.BOTTOM_EAST;
                case SOUTH -> VoxelShapes.BOTTOM_SOUTH;
                case WEST -> VoxelShapes.BOTTOM_WEST;
                default -> super.getShape(state, level, pos, context);
            };
            default -> super.getShape(state, level, pos, context);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(CRUCIBLE_WALL, FACING);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CrucibleWallBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrucibleWallBlockEntity(blockPos, blockState);
    }

    @Override
    public Optional<Item> getDropItem() {
        return Optional.of(IRBlocks.TERRACOTTA_BRICK.get().asItem());
    }

    @Override
    public void displayOverlay(List<Component> displayText, BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        CrucibleWallBlockEntity blockEntity = (CrucibleWallBlockEntity) level.getBlockEntity(scannedBlockPos);
        if (blockEntity.getControllerPos().isPresent()) {
            BlockPos pos = blockEntity.getControllerPos().get();
            CrucibleBlockEntity controllerBlockEntity = (CrucibleBlockEntity) level.getBlockEntity(pos);
            displayText.addAll(DisplayUtils.displayHeatInfo(controllerBlockEntity, level.getBlockState(pos), Component.literal("Crucible")));
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

        public static final VoxelShape MIDDLE_NORTH = Shapes.or(Block.box(0, 0, 0, 16, 16, 3), Block.box(0, 0, 3, 3, 16, 16));
        public static final VoxelShape MIDDLE_EAST = Shapes.or(Block.box(13, 0, 0, 16, 16, 16), Block.box(0, 0, 0, 13, 16, 3));
        public static final VoxelShape MIDDLE_SOUTH = Shapes.or(Block.box(0, 0, 13, 16, 16, 16), Block.box(13, 0, 0, 16, 16, 13));
        public static final VoxelShape MIDDLE_WEST = Shapes.or(Block.box(0, 0, 0, 3, 16, 16), Block.box(3, 0, 13, 16, 16, 16));

        public static final VoxelShape BOTTOM_BASE = Block.box(0, 4, 0, 16, 6, 16);

        public static final VoxelShape BOTTOM_NORTH = Shapes.or(BOTTOM_BASE, Block.box(0, 4, 0, 16, 16, 3), Block.box(0, 4, 3, 3, 16, 16));
        public static final VoxelShape BOTTOM_EAST = Shapes.or(BOTTOM_BASE, Block.box(13, 4, 0, 16, 16, 16), Block.box(0, 4, 0, 13, 16, 3));
        public static final VoxelShape BOTTOM_SOUTH = Shapes.or(BOTTOM_BASE, Block.box(0, 4, 13, 16, 16, 16), Block.box(13, 4, 0, 16, 16, 13));
        public static final VoxelShape BOTTOM_WEST = Shapes.or(BOTTOM_BASE, Block.box(0, 4, 0, 3, 16, 16), Block.box(3, 4, 13, 16, 16, 16));
    }
}
