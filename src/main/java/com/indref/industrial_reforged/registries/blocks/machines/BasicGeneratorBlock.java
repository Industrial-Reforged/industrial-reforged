package com.indref.industrial_reforged.registries.blocks.machines;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.machines.BasicGeneratorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BasicGeneratorBlock extends GeneratorBlock {
    public BasicGeneratorBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BasicGeneratorBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BasicGeneratorBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public Optional<BlockEntityType<? extends ContainerBlockEntity>> getBlockEntity() {
        return Optional.ofNullable(IRBlockEntityTypes.BASIC_GENERATOR.get());
    }
}
