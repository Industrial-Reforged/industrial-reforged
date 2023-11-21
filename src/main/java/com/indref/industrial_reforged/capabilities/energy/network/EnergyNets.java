package com.indref.industrial_reforged.capabilities.energy.network;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.CableBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EnergyNets implements IEnergyNets {
    private List<EnergyNet> enets;
    private final Level level;

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
        {
            EnergyNet net = getNetwork(pos);
            if (net != null) {
                if (is(EnergyNet.EnergyTypes.TRANSMITTERS, pos)) {
                    net.add(pos, EnergyNet.EnergyTypes.TRANSMITTERS);
                } else if (is(EnergyNet.EnergyTypes.PRODUCERS, pos)) {
                    net.add(pos, EnergyNet.EnergyTypes.PRODUCERS);
                } else if (is(EnergyNet.EnergyTypes.CONSUMERS, pos)) {
                    net.add(pos, EnergyNet.EnergyTypes.CONSUMERS);
                }
                return getNetwork(pos);
            }
        }
        EnergyNet newNet = EnergyNet.createNetworkAt(pos, this.level);
        enets.add(newNet);
        return newNet;
    }

    /**
     * merges two energy nets into one
     * (the one that is supplied first as an argument)
     *
     * @param originNet  the main net that the other net will be merged into
     * @param toMergeNet the net that will get merged into originNet
     */
    @Override
    public void mergeNets(EnergyNet originNet, EnergyNet toMergeNet) {
        if (originNet.getEnergyTier() == toMergeNet.getEnergyTier()) {
            originNet.get(EnergyNet.EnergyTypes.PRODUCERS).addAll(toMergeNet.get(EnergyNet.EnergyTypes.PRODUCERS));
            originNet.get(EnergyNet.EnergyTypes.CONSUMERS).addAll(toMergeNet.get(EnergyNet.EnergyTypes.CONSUMERS));
            originNet.get(EnergyNet.EnergyTypes.TRANSMITTERS).addAll(toMergeNet.get(EnergyNet.EnergyTypes.TRANSMITTERS));
            enets.remove(toMergeNet);
        }
    }

    @Override
    public void splitNets(BlockPos removedBlockPos) {
        // These are all transmitters that have been checked
        Set<BlockPos> alreadyChecked = new HashSet<>();
        // Every array entry represents the energy network created from checking each side.
        // Some of these might be empty due to there not being an exclusive enet on one side of the 'removedBlockPos'
        EnergyNet[] enets = new EnergyNet[6];
        // enets array index
        int index = 0;
        // Loop through all blocks around the removed position
        for (BlockPos offsetPos : BlockUtils.getBlocksAroundSelf(removedBlockPos)) {
            // Check if one of the blocks is a transmitter of energy
            if (level.getBlockEntity(offsetPos) instanceof CableBlockEntity && !alreadyChecked.contains(offsetPos)) {
                enets[index] = new EnergyNet(level);
                enets[index].get(EnergyNet.EnergyTypes.TRANSMITTERS).add(removedBlockPos);
                recheckConnections(offsetPos, EnergyNet.EnergyTypes.TRANSMITTERS, enets[index], alreadyChecked);
                enets[index].get(EnergyNet.EnergyTypes.TRANSMITTERS).remove(removedBlockPos);
                if (!enets[index].get(EnergyNet.EnergyTypes.TRANSMITTERS).isEmpty()) {
                    this.enets.add(enets[index]);
                }
            }
            index++;
        }
    }

    private void recheckConnections(BlockPos checkFrom, EnergyNet.EnergyTypes checkedPosType, EnergyNet enet, Set<BlockPos> alreadyCheckedTracker) {
        if (!alreadyCheckedTracker.contains(checkFrom)) {
            enet.get(checkedPosType).add(checkFrom);
            if (checkedPosType== EnergyNet.EnergyTypes.TRANSMITTERS) {
                alreadyCheckedTracker.add(checkFrom);
            }
        } else {
            return;
        }
        for (BlockPos offSetPos : BlockUtils.getBlocksAroundSelf(checkFrom)) {
            if (!enet.get(EnergyNet.EnergyTypes.TRANSMITTERS).contains(offSetPos))
                if (is(EnergyNet.EnergyTypes.TRANSMITTERS, offSetPos)) {
                    recheckConnections(offSetPos, EnergyNet.EnergyTypes.TRANSMITTERS, enet, alreadyCheckedTracker);
                } else if (is(EnergyNet.EnergyTypes.CONSUMERS, offSetPos)) {
                    recheckConnections(offSetPos, EnergyNet.EnergyTypes.CONSUMERS, enet, alreadyCheckedTracker);
                } else if (is(EnergyNet.EnergyTypes.PRODUCERS, offSetPos)) {
                    recheckConnections(offSetPos, EnergyNet.EnergyTypes.PRODUCERS, enet, alreadyCheckedTracker);
                }
        }
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
            EnergyNet net = new EnergyNet(level);
            net.deserializeNBT(nbtNetwork);
            enets.add(net);
        }
    }

    public boolean is(EnergyNet.EnergyTypes type, BlockPos blockPos) {
        switch (type) {
            case TRANSMITTERS -> {
                return level.getBlockEntity(blockPos) instanceof CableBlockEntity;
            }
            case CONSUMERS -> {
                return level.getBlockEntity(blockPos) instanceof IEnergyBlock && !(level.getBlockEntity(blockPos) instanceof GeneratorBlockEntity);
            }
            case PRODUCERS -> {
                return level.getBlockEntity(blockPos) instanceof GeneratorBlockEntity;
            }
        }
        throw new IllegalStateException("Unreachable! Energy type did not match! Energy-type: "+type);
    }

    @Override
    public String toString() {
        return "EnergyNets{" +
                "enets=" + enets +
                ", level=" + level +
                '}';
    }
}
