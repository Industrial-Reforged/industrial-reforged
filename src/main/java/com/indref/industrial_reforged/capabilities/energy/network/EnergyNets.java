package com.indref.industrial_reforged.capabilities.energy.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

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
        return null;
    }

    @Override
    public EnergyNet getNetwork(BlockPos blockPos) {
        return null;
    }

    @Override
    public EnergyNet getOrCreateNetwork(BlockPos pos) {
        if (getNetwork(pos) != null) {
            return getNetwork(pos);
        }
        enets.add(new EnergyNet());
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
}
