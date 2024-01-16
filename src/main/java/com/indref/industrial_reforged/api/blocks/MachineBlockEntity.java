package com.indref.industrial_reforged.api.blocks;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.networking.data.EnergySyncData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class MachineBlockEntity extends BlockEntity implements IEnergyBlock {
    public MachineBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Override
    public void onEnergyChanged() {
        if (!level.isClientSide()) {
            PacketDistributor.ALL.noArg().send(new EnergySyncData(getEnergyStored(this), worldPosition));
        }
    }
}
