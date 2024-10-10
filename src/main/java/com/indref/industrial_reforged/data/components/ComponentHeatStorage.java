package com.indref.industrial_reforged.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record ComponentHeatStorage(int heatStored, int heatCapacity) {
    public static final ComponentHeatStorage EMPTY = new ComponentHeatStorage(0, 0);

    public static final Codec<ComponentHeatStorage> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("heat_stored").forGetter(ComponentHeatStorage::heatStored),
                    Codec.INT.fieldOf("heat_capacity").forGetter(ComponentHeatStorage::heatCapacity)
            ).apply(builder, ComponentHeatStorage::new)
    );
    public static final StreamCodec<ByteBuf, ComponentHeatStorage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ComponentHeatStorage::heatStored,
            ByteBufCodecs.INT,
            ComponentHeatStorage::heatCapacity,
            ComponentHeatStorage::new
    );

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
