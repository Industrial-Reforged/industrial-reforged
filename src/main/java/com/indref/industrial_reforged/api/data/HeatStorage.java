package com.indref.industrial_reforged.api.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record HeatStorage(int heatStored, int heatCapacity) {
    public static final HeatStorage EMPTY = new HeatStorage(0, 0);

    public static final Codec<HeatStorage> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("heat_stored").forGetter(HeatStorage::heatStored),
                    Codec.INT.fieldOf("heat_capacity").forGetter(HeatStorage::heatCapacity)
            ).apply(builder, HeatStorage::new)
    );
    public static final StreamCodec<ByteBuf, HeatStorage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            HeatStorage::heatStored,
            ByteBufCodecs.INT,
            HeatStorage::heatCapacity,
            HeatStorage::new
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeatStorage that)) return false;
        return heatStored == that.heatStored && heatCapacity == that.heatCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(heatStored, heatCapacity);
    }
}
