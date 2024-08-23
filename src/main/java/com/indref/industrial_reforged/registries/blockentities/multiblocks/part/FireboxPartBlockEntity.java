package com.indref.industrial_reforged.registries.blockentities.multiblocks.part;

import com.indref.industrial_reforged.api.multiblocks.blockentities.MultiblockPartBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.FireboxMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class FireboxPartBlockEntity extends MultiblockPartBlockEntity {
    public FireboxPartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.FIREBOX_PART.get(), blockPos, blockState);
    }

    public IItemHandler getItemHandler(Direction dir) {
        if (getBlockState().getValue(FireboxMultiblock.FIREBOX_PART) != FireboxMultiblock.PartIndex.HATCH || dir == Direction.UP || dir == Direction.DOWN)
            return null;

        BlockEntity blockEntity = level.getBlockEntity(getControllerPos());
        if (blockEntity instanceof FireboxBlockEntity fireboxBlockEntity) {
            return fireboxBlockEntity.getItemHandlerOnSide(dir);
        }

        return null;
    }
}
