package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.api.blocks.IWrenchable;
import com.indref.industrial_reforged.registries.blockentities.TestBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MachineFrameBlock extends BaseEntityBlock implements IWrenchable {
    public static final MapCodec<MachineFrameBlock> CODEC = simpleCodec(MachineFrameBlock::new);
    public MachineFrameBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new TestBlockEntity(p_153215_, p_153216_);
    }

    // TODO: 10/15/2023 Add energy tier
}
