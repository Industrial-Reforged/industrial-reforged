package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlock;
import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.TestGeneratorBE;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TestGeneratorBlock extends GeneratorBlock {
    public <T extends GeneratorBlockEntity> TestGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends GeneratorBlockEntity> getBlockEntity() {
        return IRBlockEntityTypes.TEST_GEN.get();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(TestGeneratorBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new TestGeneratorBE(p_153215_, p_153216_);
    }
}
