package com.indref.industrial_reforged.content.blocks;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.MachineBlock;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.content.blockentities.BatteryBoxBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BatteryBoxBlock extends MachineBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BatteryBoxBlock(Properties properties, Supplier<EnergyTier> energyTier) {
        super(properties, energyTier);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state != null ? state.setValue(FACING, context.getNearestLookingDirection().getOpposite()) : null;
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.BATTERY_BOX.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return machineBlockCodec(BatteryBoxBlock::new);
    }

}
