package com.indref.industrial_reforged.content.blocks.misc;

import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.blocks.misc.CanAttachFaucetBlock;
import com.indref.industrial_reforged.content.blockentities.FaucetBlockEntity;
import com.indref.industrial_reforged.networking.PowerBlockEntityPayload;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.BlockUtils;
import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableContainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FaucetBlock extends RotatableContainerBlock implements WrenchableBlock {
    private static final Map<Block, Block> ALTERNATE_VERSIONS = new HashMap<>();
    public static final VoxelShape SOUTH_SHAPE = Stream.of(
            Block.box(5, 5, 12, 11, 6, 16),
            Block.box(5, 6, 12, 6, 9, 16),
            Block.box(10, 6, 12, 11, 9, 16)
    ).reduce(Shapes::or).get();
    public static final VoxelShape WEST_SHAPE = Stream.of(
            Block.box(0, 5, 5, 4, 6, 11),
            Block.box(0, 6, 5, 4, 9, 6),
            Block.box(0, 6, 10, 4, 9, 11)
    ).reduce(Shapes::or).get();
    public static final VoxelShape NORTH_SHAPE = Stream.of(
            Block.box(5, 5, 0, 11, 6, 4),
            Block.box(10, 6, 0, 11, 9, 4),
            Block.box(5, 6, 0, 6, 9, 4)
    ).reduce(Shapes::or).get();
    public static final VoxelShape EAST_SHAPE = Stream.of(
            Block.box(12, 5, 5, 16, 6, 11),
            Block.box(12, 6, 10, 16, 9, 11),
            Block.box(12, 6, 5, 16, 9, 6)
    ).reduce(Shapes::or).get();

    public FaucetBlock(Properties properties, Block ingredient) {
        super(properties.noOcclusion());
        ALTERNATE_VERSIONS.put(ingredient, this);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch (p_60555_.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH -> SOUTH_SHAPE;
            case EAST -> WEST_SHAPE;
            case SOUTH -> NORTH_SHAPE;
            case WEST -> EAST_SHAPE;
            default -> Block.box(0, 0, 0, 0, 0, 0);
        };
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        for (Block key : ALTERNATE_VERSIONS.keySet()) {
            Block val = ALTERNATE_VERSIONS.get(key);
            if (itemStack.is(key.asItem()) && !blockState.is(val)) {
                level.setBlockAndUpdate(blockPos, val.defaultBlockState()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, direction));
                level.playSound(player, blockPos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        FaucetBlockEntity be = BlockUtils.getBE(level, pos, FaucetBlockEntity.class);
        BlockPos blockPos = pos.subtract(neighborPos);
        Direction direction = Direction.fromDelta(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (direction != null) {
            boolean flag = level.hasSignal(neighborPos, direction);
            if (level.getBlockState(neighborPos).isSignalSource() && flag != be.isPowered()) {
                be.setPowered(flag);
                if (level instanceof ServerLevel serverLevel) {
                    PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(pos), new PowerBlockEntityPayload(pos, flag));
                }
            }
        }
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends IRContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.FAUCET.get();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(props -> new FaucetBlock(props, null));
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        Direction facing = context.getHorizontalDirection().getOpposite();
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        if (level.getBlockState(clickedPos).getBlock() instanceof CanAttachFaucetBlock block) {
            BlockPos relative = clickedPos.relative(context.getClickedFace().getOpposite());
            BlockState toAttach = level.getBlockState(relative);
            if (block.canAttachFaucetToBlock(toAttach, relative, level, context.getClickedFace())) {
                state = null;
            }
        }
        return state != null ? state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing) : null;
    }
}
