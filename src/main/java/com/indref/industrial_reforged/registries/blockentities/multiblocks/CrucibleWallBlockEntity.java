package com.indref.industrial_reforged.registries.blockentities.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.SavesControllerPos;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// TODO: manually determine controllerpos based on blockstate
@Deprecated
public class CrucibleWallBlockEntity extends BlockEntity implements SavesControllerPos {
    public BlockPos controllerPos;

    public CrucibleWallBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE_WALL.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        try {
            tag.putIntArray("controllerPos", new int[]{controllerPos.getX(), controllerPos.getY(), controllerPos.getZ()});
        } catch (Exception e) {
            IndustrialReforged.LOGGER.error("Failed to save controller pos {}", e.toString());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        int[] rawPos = tag.getIntArray("controllerPos");
        controllerPos = new BlockPos(rawPos[0], rawPos[1], rawPos[2]);
    }

    @Override
    public void setControllerPos(BlockPos blockPos) {
        this.controllerPos = blockPos;
    }
}
