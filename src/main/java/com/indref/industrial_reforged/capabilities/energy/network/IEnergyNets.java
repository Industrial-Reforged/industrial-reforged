package com.indref.industrial_reforged.capabilities.energy.network;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoRegisterCapability
public interface IEnergyNets {
    List<EnergyNet> getNetworks();

    /**
     * Will check blocks around supplied blockpos (neighbors) and blockPos itself
     */
    @Nullable EnergyNet getNetwork(BlockPos blockPos);

    /**
     * Will return enet at given position and ignore neighboring enets
     */
    @Nullable EnergyNet getNetworkRaw(BlockPos blockPos);
    EnergyNet getOrCreateNetAndPush(BlockPos pos);
    void removeNetwork(BlockPos pos);
    void removeNetwork(int index);
    void resetNets();

    /**
     * merges two energy nets into one
     * (the one that is supplied first as an argument)
     * @param originNet the main net that the other net will be merged into
     * @param toMergeNet the net that will get merged into originNet
     * @return true if successful (energy tier matches)
     */
    boolean mergeNets(EnergyNet originNet, EnergyNet toMergeNet);

    EnergyNet recheckConnected(BlockPos checkPos);
}
