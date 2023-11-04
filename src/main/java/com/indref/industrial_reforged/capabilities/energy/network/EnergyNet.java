package com.indref.industrial_reforged.capabilities.energy.network;

import com.indref.industrial_reforged.api.tiers.EnergyTiers;
import com.indref.industrial_reforged.api.tiers.templates.EnergyTier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class EnergyNet {
    private final EnergyTier energyTier;
    private Set<BlockPos> transmitters;
    private Set<BlockPos> producers;
    private Set<BlockPos> consumers;
    private static final String NBT_KEY_CABLES = "cables";
    private static final String NBT_KEY_PRODUCERS = "producers";
    private static final String NBT_KEY_CONSUMERS = "consumers";
    public static final String NBT_KEY_ENERGY_TIER = "energyTier";

    public EnergyNet() {
        this.transmitters = new HashSet<>();
        this.producers = new HashSet<>();
        this.consumers = new HashSet<>();
        this.energyTier = EnergyTiers.CREATIVE;
    }

    private EnergyNet(BlockPos blockPos) {
        this.transmitters = new HashSet<>();
        this.producers = new HashSet<>();
        this.consumers = new HashSet<>();
        transmitters.add(blockPos);
        this.energyTier = EnergyTiers.CREATIVE;
    }

    public static EnergyNet createNetworkAt(BlockPos blockPos) {
        return new EnergyNet(blockPos);
    }

    public void add(BlockPos blockPos, EnergyTypes energyType) {
        switch (energyType) {
            case CONSUMER -> consumers.add(blockPos);
            case PRODUCER -> producers.add(blockPos);
            case TRANSMITTER -> transmitters.add(blockPos);
        }
    }

    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    public Set<BlockPos> getTransmitters() {
        return transmitters;
    }

    public Set<BlockPos> getConsumers() {
        return consumers;
    }

    public Set<BlockPos> getProducers() {
        return producers;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        List<Long> positions = new ArrayList<>();
        for (BlockPos pos : transmitters) {
            positions.add(pos.asLong());
        }
        tag.putLongArray(NBT_KEY_CABLES, positions);
        tag.putString(NBT_KEY_ENERGY_TIER, getEnergyTier().getName());
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        Set<BlockPos> positions = new HashSet<>();
        for (long pos : nbt.getLongArray(NBT_KEY_CABLES)) {
            positions.add(BlockPos.of(pos));
        }
        transmitters = positions;
    }

    @Override
    public String toString() {
        return "EnergyNet{" +
                "energyTier=" + energyTier +
                ", transmitters=" + transmitters +
                ", producers=" + producers +
                ", consumers=" + consumers +
                '}';
    }

    public enum EnergyTypes {
        CONSUMER,
        PRODUCER,
        TRANSMITTER,
    }
}
