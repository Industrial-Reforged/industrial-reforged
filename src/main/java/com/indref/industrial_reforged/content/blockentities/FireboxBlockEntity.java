package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FireboxBlockEntity extends BlockEntity implements IHeatBlock {
    public FireboxBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Override
    public int getCapacity() {
        return 0;
    }
}
