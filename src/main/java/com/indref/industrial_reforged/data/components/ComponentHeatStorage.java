package com.indref.industrial_reforged.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record ComponentHeatStorage(float heatStored, float lastHeatStored, float heatCapacity) {
    public static final ComponentHeatStorage EMPTY = new ComponentHeatStorage(0, 0, 0);

    public static final Codec<ComponentHeatStorage> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.FLOAT.fieldOf("heat_stored").forGetter(ComponentHeatStorage::heatStored),
                    Codec.FLOAT.fieldOf("last_heat_stored").forGetter(ComponentHeatStorage::lastHeatStored),
                    Codec.FLOAT.fieldOf("heat_capacity").forGetter(ComponentHeatStorage::heatCapacity)
            ).apply(builder, ComponentHeatStorage::new)
    );
    public static final StreamCodec<ByteBuf, ComponentHeatStorage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ComponentHeatStorage::heatStored,
            ByteBufCodecs.FLOAT,
            ComponentHeatStorage::lastHeatStored,
            ByteBufCodecs.FLOAT,
            ComponentHeatStorage::heatCapacity,
            ComponentHeatStorage::new
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentHeatStorage that)) return false;
        return heatStored == that.heatStored && heatCapacity == that.heatCapacity && lastHeatStored == that.heatStored;
    }

    @Override
    public int hashCode() {
        return Objects.hash(heatStored, lastHeatStored, heatCapacity);
    }
}
