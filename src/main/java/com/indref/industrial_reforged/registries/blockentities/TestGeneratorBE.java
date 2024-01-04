package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlockEntity;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TestGeneratorBE extends GeneratorBlockEntity {
    public TestGeneratorBE(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.TEST_GEN.get(), p_155229_, p_155230_);
    }

    @Override
    public int getGenerationAmount() {
        return 10;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return EnergyTiers.LOW;
    }
}
