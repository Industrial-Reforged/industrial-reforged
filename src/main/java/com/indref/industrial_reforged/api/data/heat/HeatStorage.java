package com.indref.industrial_reforged.api.data.heat;

import com.indref.industrial_reforged.api.data.energy.EnergyStorage;
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
public class HeatStorage implements INBTSerializable<Tag> {
    private int heat;
    private int maxHeat;

    private static final Codec<Pair<Integer, Integer>> PAIR_CODEC =
            Codec.pair(Codec.INT, Codec.INT);
    public static final Codec<HeatStorage> CODEC =
            PAIR_CODEC.xmap(pair -> new HeatStorage(pair.getFirst(), pair.getSecond()), es -> new Pair<>(es.heat, es.maxHeat));
    public static final StreamCodec<ByteBuf, HeatStorage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull HeatStorage decode(ByteBuf byteBuf) {
            int heat = byteBuf.readInt();
            int maxHeat = byteBuf.readInt();
            return new HeatStorage(heat, maxHeat);
        }

        @Override
        public void encode(ByteBuf byteBuf, HeatStorage es) {
            byteBuf.writeInt(es.heat);
            byteBuf.writeInt(es.maxHeat);
        }
    };

    public HeatStorage(int heat, int maxHeat) {
        this.heat = heat;
        this.maxHeat = maxHeat;
    }

    public int getHeatStored() {
        return heat;
    }

    public void setHeatStored(int heat) {
        this.heat = heat;
    }

    public int getHeatCapacity() {
        return maxHeat;
    }

    public void setHeatCapacity(int heat) {
        this.maxHeat = heat;
    }

    @Override
    public @UnknownNullability Tag serializeNBT(HolderLookup.Provider provider) {
        return IntTag.valueOf(this.getHeatStored());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        if (!(tag instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.heat = intNbt.getAsInt();
    }
}
