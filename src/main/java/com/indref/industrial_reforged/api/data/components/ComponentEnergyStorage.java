package com.indref.industrial_reforged.api.data.components;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Class for saving and loading heat values of item/block/entity
 * <br><br>
 * Should not be use directly,
 * only for attaching data/accessing it through capabilities
 */
public record ComponentEnergyStorage(int energy, int energyCapacity) {
    public static final ComponentEnergyStorage EMPTY = new ComponentEnergyStorage(0, 0);

    private static final Codec<Pair<Integer, Integer>> PAIR_CODEC =
            Codec.pair(Codec.INT, Codec.INT);
    public static final Codec<ComponentEnergyStorage> CODEC =
            PAIR_CODEC.xmap(pair -> new ComponentEnergyStorage(pair.getFirst(), pair.getSecond()), es -> new Pair<>(es.energy, es.energyCapacity));
    public static final StreamCodec<ByteBuf, ComponentEnergyStorage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ComponentEnergyStorage decode(ByteBuf byteBuf) {
            int energy = byteBuf.readInt();
            int maxEnergy = byteBuf.readInt();
            return new ComponentEnergyStorage(energy, maxEnergy);
        }

        @Override
        public void encode(ByteBuf byteBuf, ComponentEnergyStorage es) {
            byteBuf.writeInt(es.energy);
            byteBuf.writeInt(es.energyCapacity);
        }
    };

    public int getEnergyStored() {
        return energy;
    }

    public int getEnergyCapacity() {
        return energyCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentEnergyStorage that)) return false;
        return energy == that.energy && energyCapacity == that.energyCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(energy, energyCapacity);
    }
}
