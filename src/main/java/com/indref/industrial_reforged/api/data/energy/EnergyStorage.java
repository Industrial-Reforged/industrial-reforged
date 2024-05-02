package com.indref.industrial_reforged.api.data.energy;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Class for saving and loading heat values of item/block/entity
 * <br><br>
 * Should not be use directly,
 * only for attaching data/accessing it through capabilities
 */
public class EnergyStorage implements INBTSerializable<Tag> {
    private int energy;
    private int maxEnergy;

    private static final Codec<Pair<Integer, Integer>> PAIR_CODEC =
            Codec.pair(Codec.INT, Codec.INT);
    public static final Codec<EnergyStorage> CODEC =
            PAIR_CODEC.xmap(pair -> new EnergyStorage(pair.getFirst(), pair.getSecond()), es -> new Pair<>(es.energy, es.maxEnergy));
    public static final StreamCodec<ByteBuf, EnergyStorage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull EnergyStorage decode(ByteBuf byteBuf) {
            int energy = byteBuf.readInt();
            int maxEnergy = byteBuf.readInt();
            return new EnergyStorage(energy, maxEnergy);
        }

        @Override
        public void encode(ByteBuf byteBuf, EnergyStorage es) {
            byteBuf.writeInt(es.energy);
            byteBuf.writeInt(es.maxEnergy);
        }
    };

    public EnergyStorage(int energy, int maxEnergy) {
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    public int getEnergyStored() {
        return energy;
    }

    public void setEnergyStored(int val) {
        this.energy = val;
    }

    public int getEnergyCapacity() {
        return maxEnergy;
    }

    public void setEnergyCapacity(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    @Override
    public @UnknownNullability Tag serializeNBT(HolderLookup.Provider provider) {
        return IntTag.valueOf(this.getEnergyStored());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        if (!(tag instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energy = intNbt.getAsInt();
    }
}
