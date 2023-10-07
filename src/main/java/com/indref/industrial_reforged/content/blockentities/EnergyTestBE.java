package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageCapability;
import com.indref.industrial_reforged.api.energy.IEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyTestBE extends BlockEntity implements IEnergyStorage {

    public EnergyTestBE(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.ENERGY_TEST.get(), blockPos, blockState);
    }

    @Override
    public EnergyStorageCapability getEnergyStorage() {
        return new EnergyStorageCapability(69, 69);
    }
}
