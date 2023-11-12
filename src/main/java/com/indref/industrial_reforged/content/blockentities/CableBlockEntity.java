package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.content.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CableBlockEntity extends BlockEntity implements IEnergyBlock {
    private final BlockPos blockPos;
    public CableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CABLE.get(), blockPos, blockState);
        this.blockPos = blockPos;
    }

    @Override
    public int getCapacity() {
        return 1000;
    }

    @Nullable
    public EnergyNet getEnergyNet(Level level) {
        return Util.getEnergyNets(level).getNetwork(blockPos);
    }
}
