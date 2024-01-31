package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrucibleWallBlockEntity extends BlockEntity {
    public BlockPos controllerPos;

    public CrucibleWallBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE_WALL.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putIntArray("controllerPos", new int[]{controllerPos.getX(), controllerPos.getY(), controllerPos.getZ()});
    }

    @Override
    public void load(CompoundTag tag) {
        int[] rawPos = tag.getIntArray("controllerPos");
        controllerPos = new BlockPos(rawPos[0], rawPos[1], rawPos[2]);
    }
}
