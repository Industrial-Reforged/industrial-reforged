package com.indref.industrial_reforged.registries.blockentities.multiblocks;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FaucetBlockEntity extends ContainerBlockEntity {
    public FaucetBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.FAUCET.get(), p_155229_, p_155230_);
        addFluidTank(10);
    }
}
