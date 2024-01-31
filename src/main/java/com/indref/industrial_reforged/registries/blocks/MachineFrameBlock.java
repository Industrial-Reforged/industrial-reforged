package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.registries.blockentities.TestBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MachineFrameBlock extends Block implements Wrenchable {
    public MachineFrameBlock(Properties properties) {
        super(properties);
    }


    // TODO: 10/15/2023 Add energy tier
}
