package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.RotatableContainerBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.tiers.CrucibleTiers;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CrucibleControllerBlock extends RotatableContainerBlock implements DisplayBlock {
    public static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    private final CrucibleTier tier;

    public CrucibleControllerBlock(Properties properties, CrucibleTier crucibleTier) {
        super(properties);
        this.tier = crucibleTier;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!state.is(oldState.getBlock())) {
            Direction facing =  state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            BlockPos prevPos = pos.relative(facing);
            for (int i = 0; i < 4; i++) {
                placeBlocks(level, prevPos, facing, i != 3);
                prevPos = prevPos.relative(facing.getClockWise());
                facing = facing.getClockWise();
            }

        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    private static void placeBlocks(Level level, BlockPos pos, Direction facing, boolean replaceController) {
        if (replaceController) {
            level.setBlockAndUpdate(pos, IRBlocks.CERAMIC_CRUCIBLE_WALL.get().defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, facing)
                    .setValue(CrucibleMultiblock.CRUCIBLE_WALL, CrucibleMultiblock.WallStates.BOTTOM));
        }
        level.setBlockAndUpdate(pos.above(), IRBlocks.CERAMIC_CRUCIBLE_WALL.get().defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, facing)
                .setValue(CrucibleMultiblock.CRUCIBLE_WALL, CrucibleMultiblock.WallStates.MIDDLE));
        level.setBlockAndUpdate(pos.above(2), IRBlocks.CERAMIC_CRUCIBLE_WALL.get().defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, facing)
                .setValue(CrucibleMultiblock.CRUCIBLE_WALL, CrucibleMultiblock.WallStates.TOP));
    }

    public CrucibleTier getTier() {
        return tier;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return tier.getController().asItem().getDefaultInstance();
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise()) {
            case NORTH -> CrucibleWallBlock.VoxelShapes.BOTTOM_NORTH;
            case EAST -> CrucibleWallBlock.VoxelShapes.BOTTOM_EAST;
            case SOUTH -> CrucibleWallBlock.VoxelShapes.BOTTOM_SOUTH;
            case WEST -> CrucibleWallBlock.VoxelShapes.BOTTOM_WEST;
            default -> super.getShape(state, level, pos, context);
        };
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((properties1) -> new CrucibleControllerBlock(properties1, tier));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        MultiblockHelper.unform(IRMultiblocks.CRUCIBLE_CERAMIC.get(), blockPos, level);

        super.onRemove(blockState, level, blockPos, newState, p_60519_);
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.CRUCIBLE.get();
    }

    @Override
    public void displayOverlay(List<Component> displayText, BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        displayText.addAll(DisplayUtils.displayHeatInfo(level.getBlockEntity(scannedBlockPos), scannedBlock, Component.literal("Crucible")));
    }

    @Override
    public List<DisplayItem> getCompatibleItems() {
        return List.of(IRItems.THERMOMETER.get());
    }
}
