package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.blocks.IWrenchable;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class CrucibleWallBlock extends Block implements IWrenchable {
    public static final EnumProperty<WallStates> CRUCIBLE_WALL = EnumProperty.create("crucible_wall", WallStates.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final CrucibleTier tier;

    public CrucibleWallBlock(Properties properties, CrucibleTier crucibleTier) {
        super(properties);
        this.tier = crucibleTier;
    }

    public CrucibleTier getTier() {
        return tier;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return Items.TERRACOTTA.getDefaultInstance();
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(CRUCIBLE_WALL, FACING);
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
