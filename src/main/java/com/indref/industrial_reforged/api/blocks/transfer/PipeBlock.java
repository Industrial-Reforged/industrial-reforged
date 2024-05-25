package com.indref.industrial_reforged.api.blocks.transfer;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class PipeBlock extends Block implements Wrenchable {
    public static final BooleanProperty[] CONNECTION = new BooleanProperty[6];
    public final int border;
    public final VoxelShape shapeCenter;
    public final VoxelShape shapeD;
    public final VoxelShape shapeU;
    public final VoxelShape shapeN;
    public final VoxelShape shapeS;
    public final VoxelShape shapeW;
    public final VoxelShape shapeE;
    public final VoxelShape[] shapes;

    static {
        for (Direction dir : Direction.values()) {
            CONNECTION[dir.get3DDataValue()] = BooleanProperty.create(dir.getSerializedName());
        }
    }

    public PipeBlock(Properties properties, int width) {
        super(properties.noOcclusion());
        registerDefaultState(getStateDefinition().any()
                .setValue(CONNECTION[0], false)
                .setValue(CONNECTION[1], false)
                .setValue(CONNECTION[2], false)
                .setValue(CONNECTION[3], false)
                .setValue(CONNECTION[4], false)
                .setValue(CONNECTION[5], false)
        );
        border = (16 - width)/2;
        int B0 = border;
        int B1 = 16 - border;
        shapeCenter = box(B0, B0, B0, B1, B1, B1);
        shapeD = box(B0, 0, B0, B1, B0, B1);
        shapeU = box(B0, B1, B0, B1, 16, B1);
        shapeN = box(B0, B0, 0, B1, B1, B0);
        shapeS = box(B0, B0, B1, B1, B1, 16);
        shapeW = box(0, B0, B0, B0, B1, B1);
        shapeE = box(B1, B0, B0, 16, B1, B1);
        shapes = new VoxelShape[64];
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        int index = 0;

        for (Direction direction : Direction.values()) {
            if (blockState.getValue(CONNECTION[direction.ordinal()])) {
                index |= 1 << direction.ordinal();
            }
        }

        return getShape(index);
    }

    public VoxelShape getShape(int i) {
        if (shapes[i] == null) {
            shapes[i] = shapeCenter;

            if (((i >> 0) & 1) != 0) {
                shapes[i] = Shapes.or(shapes[i], shapeD);
            }

            if (((i >> 1) & 1) != 0) {
                shapes[i] = Shapes.or(shapes[i], shapeU);
            }

            if (((i >> 2) & 1) != 0) {
                shapes[i] = Shapes.or(shapes[i], shapeN);
            }

            if (((i >> 3) & 1) != 0) {
                shapes[i] = Shapes.or(shapes[i], shapeS);
            }

            if (((i >> 4) & 1) != 0) {
                shapes[i] = Shapes.or(shapes[i], shapeW);
            }

            if (((i >> 5) & 1) != 0) {
                shapes[i] = Shapes.or(shapes[i], shapeE);
            }
        }

        return shapes[i];
    }

    // Check for newly added blocks
    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction facingDirection, BlockState facingBlockState, LevelAccessor level, BlockPos blockPos, BlockPos facingBlockPos) {
        int connectionIndex = facingDirection.ordinal();
        Optional<BlockEntity> blockEntity = BlockUtils.blockEntityAt(level, facingBlockPos);
        if (canConnectToPipe(facingBlockState) || (blockEntity.isPresent() && canConnectTo(blockEntity.get()))) {
            return blockState.setValue(CONNECTION[connectionIndex], true);
        } else if (facingBlockState.is(Blocks.AIR)) {
            return blockState.setValue(CONNECTION[connectionIndex], false);
        }
        return blockState;
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = defaultBlockState();

        for (Direction direction : Direction.values()) {
            int connectionIndex = direction.ordinal();
            BlockPos facingBlockPos = blockPos.relative(direction);
            Optional<BlockEntity> blockEntity = BlockUtils.blockEntityAt(level, facingBlockPos);

            if (blockEntity.isPresent() && canConnectTo(blockEntity.get())) {
                blockState = blockState.setValue(CONNECTION[connectionIndex], true);
            }
        }

        IndustrialReforged.LOGGER.info("Cable: {}", blockState);

        return blockState;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTION[0], CONNECTION[1], CONNECTION[2], CONNECTION[3], CONNECTION[4], CONNECTION[5]);
    }

    public abstract boolean canConnectToPipe(BlockState connectTo);

    public abstract boolean canConnectTo(@Nullable BlockEntity connectTo);
}
