package com.indref.industrial_reforged.test.energy;

import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlockEntity;
import com.indref.industrial_reforged.content.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TestGeneratorBE extends GeneratorBlockEntity {
    public TestGeneratorBE(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.TEST_GENERATOR.get(), blockPos, blockState);
    }

    @Override
    public int getCapacity() {
        return 1_000;
    }

    @Override
    public int getGenerationAmount() {
        return 10;
    }
}
