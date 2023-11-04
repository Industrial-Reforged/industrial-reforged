package com.indref.industrial_reforged.capabilities.energy.network;

import com.indref.industrial_reforged.IndustrialReforged;
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
    private Set<BlockPos> blocks;
    private static final String NBT_KEY_BLOCKS = "blocks";
    public static final String NBT_KEY_ENERGY_TIER = "energyTier";

    public EnergyNet() {
        this.blocks = new HashSet<>();
        this.energyTier = EnergyTiers.CREATIVE;
    }

    private EnergyNet(BlockPos blockPos) {
        this.blocks = new HashSet<>();
        blocks.add(blockPos);
        this.energyTier = EnergyTiers.CREATIVE;
    }

    public static EnergyNet createNetworkAt(BlockPos blockPos) {
        return new EnergyNet(blockPos);
    }

    public EnergyTier getTier() {
        return this.energyTier;
    }

    public Set<BlockPos> getBlocks() {
        return blocks;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        List<Long> positions = new ArrayList<>();
        for (BlockPos pos : blocks) {
            positions.add(pos.asLong());
        }
        tag.putLongArray(NBT_KEY_BLOCKS, positions);
        tag.putString(NBT_KEY_ENERGY_TIER, getTier().getName());
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        Set<BlockPos> positions = new HashSet<>();
        for (long pos : nbt.getLongArray(NBT_KEY_BLOCKS)) {
            positions.add(BlockPos.of(pos));
        }
        IndustrialReforged.LOGGER.info("Deserialized positions: "+positions);
        blocks = positions;
    }

    @Override
    public String toString() {
        return "EnergyNet{" +
                "energyTier=" + energyTier +
                ", blocks=" + blocks +
                '}';
    }
}
