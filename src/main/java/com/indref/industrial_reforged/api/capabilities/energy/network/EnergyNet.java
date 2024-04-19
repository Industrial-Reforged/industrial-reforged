package com.indref.industrial_reforged.api.capabilities.energy.network;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlock;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.blocks.CableBlock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EnergyNet {
    private Optional<EnergyTier> energyTier;
    private Set<BlockPos> transmitters;
    private Set<BlockPos> interactors;
    private final Level level;
    private static final String NBT_KEY_TRANSMITTERS = "cables";
    private static final String NBT_KEY_INTERACTORS = "interactors";

    public EnergyNet(Level level) {
        this.transmitters = new HashSet<>();
        this.interactors = new HashSet<>();
        this.energyTier = Optional.empty();
        this.level = level;
    }

    private EnergyNet(BlockPos blockPos, Level level) {
        this.transmitters = new HashSet<>();
        this.interactors = new HashSet<>();
        transmitters.add(blockPos);
        if (level.getBlockState(blockPos).getBlock() instanceof CableBlock cableBlock) {
            this.energyTier = Optional.of(cableBlock.getEnergyTier());
        } else {
            this.energyTier = Optional.empty();
        }
        this.level = level;
    }

    public static EnergyNet createNetworkAt(BlockPos blockPos, Level level) {
        return new EnergyNet(blockPos, level);
    }

    public Optional<EnergyTier> getEnergyTier() {
        return this.energyTier;
    }

    public void add(BlockPos blockPos, EnergyTypes energyType) {
        switch (energyType) {
            case INTERACTORS -> interactors.add(blockPos);
            case TRANSMITTERS -> transmitters.add(blockPos);
        }
    }

    public void addAll(Set<BlockPos> blockPoses, EnergyTypes energyType) {
        switch (energyType) {
            case INTERACTORS -> interactors.addAll(blockPoses);
            case TRANSMITTERS -> transmitters.addAll(blockPoses);
        }
    }

    public Set<BlockPos> get(EnergyTypes energyType) {
        return switch (energyType) {
            case INTERACTORS -> interactors;
            case TRANSMITTERS -> transmitters;
        };
    }

    public Set<BlockPos> getAll() {
        Set<BlockPos> all = new HashSet<>();
        all.addAll(transmitters);
        all.addAll(interactors);
        return all;
    }

    public void remove(BlockPos blockPos, EnergyTypes energyTypes) {
        switch (energyTypes) {
            case INTERACTORS -> interactors.remove(blockPos);
            case TRANSMITTERS -> transmitters.remove(blockPos);
        }
    }

    /**
     * Get all blockpositions of interactors
     * that can accept energy
     */
    public List<BlockPos> getEnergyAcceptors() {
        for (BlockPos blockPos : interactors) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (BlockUtils.isEnergyBlock(blockEntity)) {
                IEnergyBlock energyBlock = (IEnergyBlock) blockEntity;
                if (energyBlock.getEnergyStored(blockEntity) < energyBlock.getEnergyCapacity()) {

                }
            }
        }
        return List.of();
    }

    /**
     * Distribute energy evenly in this energynet
     * @param amount specify the amount of energy that should be distributed
     * @return whether the energy was distributed
     */
    public boolean distributeEnergy(int amount) {
        List<BlockPos> interactors = this.interactors.stream().toList();
        List<BlockPos> consumers = new ArrayList<>();
        // check for potential consumers
        for (BlockPos blockPos : interactors) {
            if (!(level.getBlockState(blockPos).getBlock() instanceof GeneratorBlock)) {
                consumers.add(blockPos);
            }
        }

        if (consumers.isEmpty()) return false;

        List<BlockPos> finalConsumers = new ArrayList<>();
        int[] initialAmount = Utils.splitNumberEvenly(amount, consumers.size());

        // check which blocks can accept the energy
        for (int i = 0; i < consumers.size(); i++) {
            BlockPos blockPos = consumers.get(i);
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (BlockUtils.isEnergyBlock(blockEntity)) {
                if (((IEnergyBlock) blockEntity).canAcceptEnergy(blockEntity, initialAmount[i]))
                    finalConsumers.add(blockPos);
            }
        }

        if (finalConsumers.isEmpty()) return false;

        int[] finalAmount = Utils.splitNumberEvenly(amount, finalConsumers.size());

        // distribute energy
        for (int i = 0; i < finalConsumers.size(); i++) {
            BlockPos blockPos = finalConsumers.get(i);
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (BlockUtils.isEnergyBlock(blockEntity))
                ((IEnergyBlock) blockEntity).tryFillEnergy(blockEntity, finalAmount[i]);
        }

        return true;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        List<Long> tPositions = new ArrayList<>();
        List<Long> iPositions = new ArrayList<>();
        for (BlockPos pos : transmitters) {
            tPositions.add(pos.asLong());
        }
        for (BlockPos pos : interactors) {
            iPositions.add(pos.asLong());
        }
        tag.putLongArray(NBT_KEY_TRANSMITTERS, tPositions);
        tag.putLongArray(NBT_KEY_INTERACTORS, iPositions);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        Set<BlockPos> tPositions = new HashSet<>();
        Set<BlockPos> iPositions = new HashSet<>();
        for (long pos : nbt.getLongArray(NBT_KEY_TRANSMITTERS)) {
            tPositions.add(BlockPos.of(pos));
        }
        for (long pos : nbt.getLongArray(NBT_KEY_INTERACTORS)) {
            iPositions.add(BlockPos.of(pos));
        }
        transmitters = tPositions;
        interactors = iPositions;
        if (!transmitters.isEmpty()) {
            if (level.getBlockState(transmitters.stream().findFirst().get()).getBlock() instanceof CableBlock cableBlock) {
                this.energyTier = Optional.of(cableBlock.getEnergyTier());
                return;
            }
        }
        this.energyTier = Optional.empty();
    }

    @Override
    public String toString() {
        return "EnergyNet{" +
                "energyTier=" + energyTier +
                ", transmitters=" + transmitters +
                ", interactors=" + interactors +
                '}';
    }

    public enum EnergyTypes {
        /**
         * This interacts with the enet,
         * meaning it will drain or fill
         * the enet with energy
         */
        INTERACTORS,
        /**
         * This transmits energy through the enet
         * and most likely is a cable
         */
        TRANSMITTERS,
    }
}
