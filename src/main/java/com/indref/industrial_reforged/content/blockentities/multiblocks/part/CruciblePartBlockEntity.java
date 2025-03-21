package com.indref.industrial_reforged.content.blockentities.multiblocks.part;

import com.indref.industrial_reforged.api.blockentities.multiblock.MultiblockPartBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class CruciblePartBlockEntity extends MultiblockPartBlockEntity {
    public CruciblePartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE_PART.get(), blockPos, blockState);
    }

    public IItemHandler exposeItemHandler(Direction direction) {
        CrucibleBlockEntity be = BlockUtils.getBE(level, getControllerPos(), CrucibleBlockEntity.class);
        return be != null ? be.getItemHandlerOnSide(direction) : null;
    }

    public IFluidHandler exposeFluidHandler(Direction direction) {
        CrucibleBlockEntity be = BlockUtils.getBE(level, getControllerPos(), CrucibleBlockEntity.class);
        return be != null ? be.getFluidHandlerOnSide(direction) : null;
    }
}
