package com.indref.industrial_reforged.api.blocks.transfer;

import com.indref.industrial_reforged.api.blocks.IWrenchable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public abstract class PipeBlock extends BaseEntityBlock implements IWrenchable {
    public static final BooleanProperty[] CONNECTION = new BooleanProperty[6];

    static {
        for (Direction dir : Direction.values()) {
            CONNECTION[dir.get3DDataValue()] = BooleanProperty.create(dir.getSerializedName());
        }
    }

    public PipeBlock(Properties properties) {
        super(properties.noOcclusion());
        registerDefaultState(getStateDefinition().any()
                .setValue(CONNECTION[0], false)
                .setValue(CONNECTION[1], false)
                .setValue(CONNECTION[2], false)
                .setValue(CONNECTION[3], false)
                .setValue(CONNECTION[4], false)
                .setValue(CONNECTION[5], false)
        );
    }

    // Check for newly added blocks
    @Override
    public BlockState updateShape(BlockState blockState, Direction facingDirection, BlockState facingBlockState, LevelAccessor level, BlockPos blockPos, BlockPos facingBlockPos) {
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("Updated"));
        int connectionIndex = facingDirection.ordinal();
        if (canConnectToPipe(facingBlockState.getBlock()) || canConnectTo(level.getBlockEntity(facingBlockPos))) {
            return blockState.setValue(CONNECTION[connectionIndex], true);
        } else if (facingBlockState.is(Blocks.AIR)) {
            return blockState.setValue(CONNECTION[connectionIndex], false);
        }
        return blockState;
    }

    // Check for blocks that are already there when placing the cable


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = defaultBlockState();

        for (Direction direction : Direction.values()) {
            int connectionIndex = direction.ordinal();
            BlockPos facingBlockPos = blockPos.relative(direction);
            BlockState facingBlockState = level.getBlockState(facingBlockPos);

            if (canConnectTo(level.getBlockEntity(facingBlockPos))) {
                return blockState.setValue(CONNECTION[connectionIndex], true);
            }
        }

        return blockState;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTION[0], CONNECTION[1], CONNECTION[2], CONNECTION[3], CONNECTION[4], CONNECTION[5]);
    }

    public abstract boolean canConnectToPipe(Block connectTo);
    public abstract boolean canConnectTo(@Nullable BlockEntity connectTo);
}
