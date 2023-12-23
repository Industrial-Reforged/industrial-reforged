package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class FaucetBlock extends Block implements IWrenchable {
    public static final BooleanProperty ATTACHED_TO_CRUCIBLE = BooleanProperty.create("attached_to_crucible");
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public FaucetBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch (p_60555_.getValue(FACING)) {
            case NORTH -> Stream.of(
                    Block.box(5, 5, 12, 11, 6, 16),
                    Block.box(5, 6, 12, 6, 9, 16),
                    Block.box(10, 6, 12, 11, 9, 16)
            ).reduce(Shapes::or).get();
            case EAST -> Stream.of(
                    Block.box(0, 5, 5, 4, 6, 11),
                    Block.box(0, 6, 5, 4, 9, 6),
                    Block.box(0, 6, 10, 4, 9, 11)
            ).reduce(Shapes::or).get();
            case SOUTH -> Stream.of(
                    Block.box(5, 5, 0, 11, 6, 4),
                    Block.box(10, 6, 0, 11, 9, 4),
                    Block.box(5, 6, 0, 6, 9, 4)
            ).reduce(Shapes::or).get();
            case WEST -> Stream.of(
                    Block.box(12, 5, 5, 16, 6, 11),
                    Block.box(12, 6, 10, 16, 9, 11),
                    Block.box(12, 6, 5, 16, 9, 6)
            ).reduce(Shapes::or).get();
            default -> Block.box(0, 0, 0, 0, 0, 0);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(ATTACHED_TO_CRUCIBLE, FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().relative(context.getHorizontalDirection()));
        BlockState toReturn = super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
        if (blockState.getBlock() instanceof CrucibleWallBlock &&
                (blockState.getValue(CrucibleWallBlock.CRUCIBLE_WALL).equals(CrucibleWallBlock.WallStates.WALL_BOTTOM) ||
                        blockState.getValue(CrucibleWallBlock.CRUCIBLE_WALL).equals(CrucibleWallBlock.WallStates.EDGE_BOTTOM))) {
            return toReturn.setValue(ATTACHED_TO_CRUCIBLE, true);
        }

        return toReturn.setValue(ATTACHED_TO_CRUCIBLE, false);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }
}
