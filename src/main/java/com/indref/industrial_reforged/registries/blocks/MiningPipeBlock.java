package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.BOTTOM;

@SuppressWarnings("deprecation")
public class MiningPipeBlock extends Block implements Wrenchable {
    private static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 0, 512);

    public MiningPipeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(DISTANCE, 512)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56051_) {
        p_56051_.add(DISTANCE, BOTTOM);
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState p_56037_, BlockPlaceContext context) {
        return context.getItemInHand().is(this.asItem());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_56023_) {
        BlockPos blockpos = p_56023_.getClickedPos();
        Level level = p_56023_.getLevel();
        int i = getDistance(level, blockpos);
        return this.defaultBlockState()
                .setValue(DISTANCE, i)
                .setValue(BOTTOM, this.isBottom(level, blockpos, i));
    }

    @Override
    public void onPlace(BlockState p_56062_, Level p_56063_, BlockPos p_56064_, BlockState p_56065_, boolean p_56066_) {
        if (!p_56063_.isClientSide) {
            p_56063_.scheduleTick(p_56064_, this, 1);
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_56044_, Direction p_56045_, BlockState p_56046_, LevelAccessor p_56047_, BlockPos p_56048_, BlockPos p_56049_) {
        if (!p_56047_.isClientSide()) {
            p_56047_.scheduleTick(p_56048_, this, 1);
        }

        return p_56044_;
    }

    private boolean isBottom(BlockGetter p_56028_, BlockPos p_56029_, int p_56030_) {
        return p_56030_ > 0 && !p_56028_.getBlockState(p_56029_.below()).is(this);
    }

    public static int getDistance(BlockGetter p_56025_, BlockPos p_56026_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_56026_.mutable().move(Direction.DOWN);
        BlockState blockstate = p_56025_.getBlockState(blockpos$mutableblockpos);
        int i = 7;
        if (blockstate.is(IRBlocks.MINING_PIPE.get())) {
            i = blockstate.getValue(DISTANCE);
        } else if (blockstate.isFaceSturdy(p_56025_, blockpos$mutableblockpos, Direction.UP)) {
            return 0;
        }

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockstate1 = p_56025_.getBlockState(blockpos$mutableblockpos.setWithOffset(p_56026_, direction));
            if (blockstate1.is(IRBlocks.MINING_PIPE.get())) {
                i = Math.min(i, blockstate1.getValue(DISTANCE) + 1);
                if (i == 1) {
                    break;
                }
            }
        }

        return i;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    }
}
