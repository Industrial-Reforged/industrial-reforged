package com.indref.industrial_reforged.api.blocks.transfer;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.content.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CableBlockEntity extends BlockEntity implements IEnergyBlock {
    public CableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CABLE.get(), blockPos, blockState);
    }

    @Override
    public int getCapacity() {
        return 1000;
    }
}
