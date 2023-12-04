package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HeatTestBE extends BlockEntity implements IHeatBlock {
    public HeatTestBE(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.HEAT_TEST.get(), blockPos, blockState);
    }

    @Override
    public int getHeatCapacity() {
        return 10000;
    }
}
