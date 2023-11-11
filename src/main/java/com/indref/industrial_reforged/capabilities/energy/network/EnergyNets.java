package com.indref.industrial_reforged.capabilities.energy.network;

import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnergyNets implements IEnergyNets {
    private List<EnergyNet> enets;
    private final Level level;

    private final String NBT_KEY_NETWORKS = "energyNetworks";

    /**
     * Initializes energy networks of the world
     */
    public EnergyNets(Level level) {
        this.level = level;
        this.enets = new ArrayList<>();
    }

    @Override
    public List<EnergyNet> getNetworks() {
        return this.enets;
    }

    @Override
    @Nullable
    public EnergyNet getNetworkRaw(BlockPos blockPos) {
        for (EnergyNet enet : getNetworks()) {
            if (enet.get(EnergyNet.EnergyTypes.TRANSMITTERS).contains(blockPos))
                return enet;
        }
        return null;
    }

    @Override
    @Nullable
    public EnergyNet getNetwork(BlockPos blockPos) {
        EnergyNet rawNet = getNetworkRaw(blockPos);

        if (rawNet != null) return rawNet;

        for (EnergyNet enet : getNetworks()) {
            for (BlockPos pos : BlockUtils.getBlocksAroundSelf(blockPos)) {
                if (enet.get(EnergyNet.EnergyTypes.TRANSMITTERS).contains(pos))
                    return enet;
            }
        }
        return null;
    }

    @Override
    public EnergyNet getOrCreateNetAndPush(BlockPos pos) {
        // create a scope to prevent using net variable
        {
            EnergyNet net = getNetwork(pos);
            if (net != null) {
                net.add(pos, EnergyNet.EnergyTypes.TRANSMITTERS);
                return getNetwork(pos);
            }
        }
        EnergyNet newNet = EnergyNet.createNetworkAt(pos);
        enets.add(newNet);
        return newNet;
    }

    /**
     * merges two energy nets into one
     * (the one that is supplied first as an argument)
     * @param originNet the main net that the other net will be merged into
     * @param toMergeNet the net that will get merged into originNet
     * @return true if successful (energy tier matches)
     */
    @Override
    public boolean mergeNets(EnergyNet originNet, EnergyNet toMergeNet) {
        if (originNet.getEnergyTier() == toMergeNet.getEnergyTier()) {
            originNet.get(EnergyNet.EnergyTypes.PRODUCERS).addAll(toMergeNet.get(EnergyNet.EnergyTypes.PRODUCERS));
            originNet.get(EnergyNet.EnergyTypes.CONSUMERS).addAll(toMergeNet.get(EnergyNet.EnergyTypes.CONSUMERS));
            originNet.get(EnergyNet.EnergyTypes.TRANSMITTERS).addAll(toMergeNet.get(EnergyNet.EnergyTypes.TRANSMITTERS));
            enets.remove(toMergeNet);
            return true;
        }
        return false;
    }

    @Override
    public void removeNetwork(BlockPos pos) {
        if (getNetwork(pos) != null) {
            enets.remove(getNetwork(pos));
        }
    }

    @Override
    public void removeNetwork(int index) {
        if (getNetworks().get(index) != null) {
            enets.remove(getNetworks().get(index));
        }
    }

    @Override
    public void resetNets() {
        this.enets = new ArrayList<>();
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        int index = 0;
        for (EnergyNet net : enets) {
            tag.put(String.valueOf(index++), net.serializeNBT());
        }
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        for (String key : nbt.getAllKeys()) {
            CompoundTag nbtNetwork = nbt.getCompound(key);
            EnergyNet net = new EnergyNet();
            net.deserializeNBT(nbtNetwork);
            enets.add(net);
        }
    }

    @Override
    public String toString() {
        return "EnergyNets{" +
                "enets=" + enets +
                ", level=" + level +
                '}';
    }
}
