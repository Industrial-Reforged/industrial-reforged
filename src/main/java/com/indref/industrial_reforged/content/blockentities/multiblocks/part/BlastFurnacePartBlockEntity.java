package com.indref.industrial_reforged.content.blockentities.multiblocks.part;

import com.indref.industrial_reforged.api.blockentities.multiblock.MultiblockPartBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class BlastFurnacePartBlockEntity extends MultiblockPartBlockEntity {
    public BlastFurnacePartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.BLAST_FURNACE_PART.get(), blockPos, blockState);
    }

    public IItemHandler exposeItemHandler(Direction direction) {
        BlastFurnaceBlockEntity be = BlockUtils.getBE(level, getControllerPos(), BlastFurnaceBlockEntity.class);
        return be.getItemHandlerOnSide(direction);
    }

    public IFluidHandler exposeFluidHandler(Direction direction) {
        BlastFurnaceBlockEntity be = BlockUtils.getBE(level, getControllerPos(), BlastFurnaceBlockEntity.class);
        return be.getFluidHandlerOnSide(direction);
    }

}
