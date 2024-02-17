package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CrucibleWallBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrucibleWallBlock extends BaseEntityBlock implements Wrenchable {
    public static final EnumProperty<WallStates> CRUCIBLE_WALL = EnumProperty.create("crucible_wall", WallStates.class);
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return IRBlocks.TERRACOTTA_BRICK.get().asItem().getDefaultInstance();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player.hasEffect(MobEffects.ABSORPTION)) {
            return defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(CRUCIBLE_WALL, WallStates.WALL_BOTTOM);
        } else if (player.hasEffect(MobEffects.BAD_OMEN)) {
            return defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(CRUCIBLE_WALL, WallStates.WALL_TOP);
        } else if (player.hasEffect(MobEffects.CONDUIT_POWER)) {
            return defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(CRUCIBLE_WALL, WallStates.EDGE_BOTTOM);
        } else {
            return defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(CRUCIBLE_WALL, WallStates.EDGE_TOP);
        }
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

    public enum WallStates implements StringRepresentable {
        EDGE_BOTTOM("edge_bottom"),
        EDGE_TOP("edge_top"),
        WALL_BOTTOM("wall_bottom"),
        WALL_TOP("wall_top");

        private final String name;

        WallStates(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    @Override
    public Item getDropItem() {
        return IRBlocks.TERRACOTTA_BRICK.get().asItem();
    }
}
