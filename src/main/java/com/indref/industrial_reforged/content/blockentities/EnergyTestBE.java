package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.content.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyTestBE extends BlockEntity implements IEnergyBlock {

    public EnergyTestBE(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.ENERGY_TEST.get(), blockPos, blockState);
    }

    @Override
    public void onEnergyChanged() {
    }

    @Override
    public int getCapacity(BlockEntity blockEntity) {
        return 10000;
    }

    @Override
    public EnergyStorageProvider getEnergyStorage() {
        return new EnergyStorageProvider();
    }
}
