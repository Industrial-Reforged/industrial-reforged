package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TestGeneratorBe extends GeneratorBlockEntity {
    public TestGeneratorBe(BlockEntityType<?> blockEntityType, BlockPos p_155229_, BlockState p_155230_) {
        super(blockEntityType, p_155229_, p_155230_);
    }

    @Override
    public int getGenerationAmount() {
        return 0;
    }
}
