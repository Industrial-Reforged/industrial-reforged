package com.indref.industrial_reforged.registries.blockentities.machines;

import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlockEntity;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BasicGeneratorBlockEntity extends GeneratorBlockEntity {
    public BasicGeneratorBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.BASIC_GENERATOR.get(), p_155229_, p_155230_);
    }

    @Override
    public EnergyTier getEnergyTier() {
        return EnergyTiers.LOW;
    }

    @Override
    public int getGenerationAmount() {
        return 10;
    }
}
