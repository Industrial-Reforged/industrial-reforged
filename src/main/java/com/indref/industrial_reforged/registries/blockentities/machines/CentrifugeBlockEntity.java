package com.indref.industrial_reforged.registries.blockentities.machines;

import com.indref.industrial_reforged.api.blocks.MachineBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CentrifugeBlockEntity extends MachineBlockEntity implements IEnergyBlock {
    public CentrifugeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CENTRIFUGE.get(), p_155229_, p_155230_);
    }

    @Override
    public EnergyTier getEnergyTier() {
        return EnergyTiers.LOW;
    }
}
