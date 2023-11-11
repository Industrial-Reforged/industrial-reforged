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
    private static final String NBT_KEY_TRANSMITTERS = "cables";
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

    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    public void add(BlockPos blockPos, EnergyTypes energyType) {
        switch (energyType) {
            case CONSUMERS -> consumers.add(blockPos);
            case PRODUCERS -> producers.add(blockPos);
            case TRANSMITTERS -> transmitters.add(blockPos);
        }
    }

    public Set<BlockPos> get(EnergyTypes energyType) {
        return switch (energyType) {
            case CONSUMERS -> consumers;
            case PRODUCERS -> producers;
            case TRANSMITTERS -> transmitters;
        };
    }

    public void remove(BlockPos blockPos, EnergyTypes energyTypes) {
        switch (energyTypes) {
            case CONSUMERS -> consumers.remove(blockPos);
            case PRODUCERS -> producers.remove(blockPos);
            case TRANSMITTERS -> transmitters.remove(blockPos);
        }
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        List<Long> tPositions = new ArrayList<>();
        List<Long> cPositions = new ArrayList<>();
        List<Long> pPositions = new ArrayList<>();
        for (BlockPos pos : transmitters) {
            tPositions.add(pos.asLong());
        }
        for (BlockPos pos : producers) {
            pPositions.add(pos.asLong());
        }
        for (BlockPos pos : consumers) {
            cPositions.add(pos.asLong());
        }
        tag.putLongArray(NBT_KEY_TRANSMITTERS, tPositions);
        tag.putLongArray(NBT_KEY_CONSUMERS, cPositions);
        tag.putLongArray(NBT_KEY_PRODUCERS, pPositions);
        tag.putString(NBT_KEY_ENERGY_TIER, getEnergyTier().getName());
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        Set<BlockPos> tPositions = new HashSet<>();
        Set<BlockPos> cPositions = new HashSet<>();
        Set<BlockPos> pPositions = new HashSet<>();
        for (long pos : nbt.getLongArray(NBT_KEY_TRANSMITTERS)) {
            tPositions.add(BlockPos.of(pos));
        }
        for (long pos : nbt.getLongArray(NBT_KEY_CONSUMERS)) {
            cPositions.add(BlockPos.of(pos));
        }
        for (long pos : nbt.getLongArray(NBT_KEY_PRODUCERS)) {
            pPositions.add(BlockPos.of(pos));
        }
        transmitters = tPositions;
        producers = pPositions;
        consumers = cPositions;
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
        CONSUMERS,
        PRODUCERS,
        TRANSMITTERS,
    }
}
