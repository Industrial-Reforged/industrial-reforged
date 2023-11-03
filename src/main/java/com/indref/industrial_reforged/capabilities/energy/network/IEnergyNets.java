package com.indref.industrial_reforged.capabilities.energy.network;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoRegisterCapability
public interface IEnergyNets {
    List<EnergyNet> getNetworks();

    @Nullable EnergyNet getNetwork(BlockPos blockPos);

    EnergyNet getOrCreateNetwork(BlockPos pos);
}
