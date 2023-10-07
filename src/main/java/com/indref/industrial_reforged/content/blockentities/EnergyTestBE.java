package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorageExposed;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyTestBE extends BlockEntity implements IEnergyStorageExposed {
    private EnergyStorageProvider energyStorage = new EnergyStorageProvider();

    public EnergyTestBE(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.ENERGY_TEST.get(), blockPos, blockState);
    }

    @Override
    public EnergyStorageProvider getEnergyStorage() {
        return energyStorage;
    }
}
