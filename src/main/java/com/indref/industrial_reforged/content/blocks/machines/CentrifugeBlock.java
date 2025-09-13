package com.indref.industrial_reforged.content.blocks.machines;

import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.MachineBlock;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRMachines;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static com.indref.industrial_reforged.util.Utils.ACTIVE;

public class CentrifugeBlock extends MachineBlock {

    public CentrifugeBlock(Properties properties, Supplier<EnergyTier> energyTier) {
        super(properties, energyTier);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ACTIVE).add(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends IRContainerBlockEntity> getBlockEntityType() {
        return IRMachines.CENTRIFUGE.getBlockEntityType();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return machineBlockCodec(CentrifugeBlock::new);
    }
}
