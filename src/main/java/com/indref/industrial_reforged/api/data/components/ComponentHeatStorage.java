package com.indref.industrial_reforged.api.data.components;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ComponentHeatStorage(int heatStored, int heatCapacity) {
    public static final ComponentHeatStorage EMPTY = new ComponentHeatStorage(0, 0);

    public static final Codec<ComponentHeatStorage> CODEC =
            Codec.pair(Codec.INT, Codec.INT).xmap(pair -> new ComponentHeatStorage(pair.getFirst(), pair.getSecond()), es -> new Pair<>(es.heatStored, es.heatCapacity));
    public static final StreamCodec<ByteBuf, ComponentHeatStorage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ComponentHeatStorage decode(ByteBuf byteBuf) {
            int heat = byteBuf.readInt();
            int maxHeat = byteBuf.readInt();
            return new ComponentHeatStorage(heat, maxHeat);
        }

        @Override
        public void encode(ByteBuf byteBuf, ComponentHeatStorage hs) {
            byteBuf.writeInt(hs.heatStored);
            byteBuf.writeInt(hs.heatCapacity);
        }
    };

    public int getHeatStored() {
        return heatStored;
    }

    public int getHeatCapacity() {
        return heatCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentHeatStorage that)) return false;
        return heatStored == that.heatStored && heatCapacity == that.heatCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(heatStored, heatCapacity);
    }
}
