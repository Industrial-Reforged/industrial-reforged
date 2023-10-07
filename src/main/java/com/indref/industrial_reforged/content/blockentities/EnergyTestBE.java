package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.api.capabilities.EnergyStorage;
import com.indref.industrial_reforged.api.energy.IEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyTestBE extends BlockEntity implements IEnergyStorage {
    public int curEnergy;

    public EnergyTestBE(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.ENERGY_TEST.get(), blockPos, blockState);
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return new EnergyStorage(curEnergy, 10000);
    }
}
