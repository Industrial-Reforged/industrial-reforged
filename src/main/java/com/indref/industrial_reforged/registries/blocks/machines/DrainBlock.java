package com.indref.industrial_reforged.registries.blocks.machines;

import com.indref.industrial_reforged.api.blocks.RotatableEntityBlock;
import com.indref.industrial_reforged.registries.blockentities.DrainBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DrainBlock extends RotatableEntityBlock {
    public DrainBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(DrainBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new DrainBlockEntity(p_153215_, p_153216_);
    }
}
