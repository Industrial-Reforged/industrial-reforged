package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.content.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyTestBE extends BlockEntity implements IEnergyBlock {

    public EnergyTestBE(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.ENERGY_TEST.get(), blockPos, blockState);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IEnergyBlock energyBlock = (IEnergyBlock) blockEntity;
        energyBlock.tryDrain(blockEntity, 1);
    }

    @Override
    public int getCapacity(BlockEntity blockEntity) {
        return 10000;
    }
}
