package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CrucibleWallBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.registries.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock.CRUCIBLE_WALL;

@SuppressWarnings("deprecation")
public class CrucibleWallBlock extends BaseEntityBlock implements Wrenchable, DisplayBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final CrucibleTier tier;

    public CrucibleWallBlock(Properties properties, CrucibleTier crucibleTier) {
        super(properties);
        this.tier = crucibleTier;
    }

    public CrucibleWallBlock(Properties properties) {
        super(properties);
        this.tier = null;
    }

    public CrucibleTier getTier() {
        return tier;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return IRBlocks.TERRACOTTA_BRICK.get().asItem().getDefaultInstance();
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if (blockEntity instanceof CrucibleWallBlockEntity crucibleWallBlockEntity) {
            MultiblockHelper.unform(IRMultiblocks.CRUCIBLE_CERAMIC.get(), crucibleWallBlockEntity.getControllerPos(), level);
        } else {
            IndustrialReforged.LOGGER.error("Failed to unform crucible, crucible wall blockentity corruption");
        }
        super.onRemove(blockState, level, blockPos, newState, p_60519_);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player.hasEffect(MobEffects.ABSORPTION)) {
            return defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(CRUCIBLE_WALL, CrucibleMultiblock.WallStates.WALL_BOTTOM);
        } else if (player.hasEffect(MobEffects.BAD_OMEN)) {
            return defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(CRUCIBLE_WALL, CrucibleMultiblock.WallStates.WALL_TOP);
        } else if (player.hasEffect(MobEffects.CONDUIT_POWER)) {
            return defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(CRUCIBLE_WALL, CrucibleMultiblock.WallStates.EDGE_BOTTOM);
        } else {
            return defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(CRUCIBLE_WALL, CrucibleMultiblock.WallStates.EDGE_TOP);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        try {
            CrucibleWallBlockEntity blockEntity = (CrucibleWallBlockEntity) pLevel.getBlockEntity(pPos);
            CrucibleBlockEntity controllerBlockEntity = (CrucibleBlockEntity) pLevel.getBlockEntity(blockEntity.getControllerPos());
            Utils.openMenu(pPlayer, controllerBlockEntity);
        } catch (Exception ignored) {
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(CRUCIBLE_WALL, FACING);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
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
    public List<Component> displayOverlay(BlockState scannedBlock, BlockPos scannedBlockPos, Level level) {
        BlockEntity wallBlockEntity = level.getBlockEntity(scannedBlockPos);

        if (wallBlockEntity instanceof CrucibleWallBlockEntity crucibleWallBlockEntity) {
            BlockPos controllerPos = crucibleWallBlockEntity.getControllerPos();
            IndustrialReforged.LOGGER.debug("ControllerPos: {}", controllerPos);
            BlockEntity controllerBlockEntity = level.getBlockEntity(controllerPos);
            return DisplayUtils.displayHeatInfo(controllerBlockEntity, scannedBlock, Component.literal("Crucible"));
        }

        return List.of();
    }

    @Override
    public List<DisplayItem> getCompatibleItems() {
        return List.of((DisplayItem) IRItems.THERMOMETER.get());
    }
}
