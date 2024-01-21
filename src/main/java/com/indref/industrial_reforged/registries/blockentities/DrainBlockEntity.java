package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class DrainBlockEntity extends BlockEntity {
    private final FluidTank fluidTank = new FluidTank(16000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            level.setBlockAndUpdate(worldPosition, getBlockState());
        }
    };

    public DrainBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.DRAIN.get(), p_155229_, p_155230_);
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        FluidState fluidOnTop = level.getFluidState(blockPos.above());
        if (fluidOnTop.is(FluidTags.WATER) && fluidOnTop.isSource()) {
            if (level.getGameTime() % 20 == 0) {
                level.setBlockAndUpdate(blockPos.above(), Blocks.AIR.defaultBlockState());
                fluidTank.fill(new FluidStack(fluidOnTop.getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        fluidTank.writeToNBT(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        fluidTank.readFromNBT(tag);
    }
}
