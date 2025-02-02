package com.indref.industrial_reforged.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

/**
 * Class for saving and loading energyStored values of items
 * <br><br>
 * Should not be use directly,
 * only for attaching data/accessing it through capabilities
 */
public record ComponentEuStorage(int energyStored, int energyCapacity) {
    public static final ComponentEuStorage EMPTY = new ComponentEuStorage(0, 0);
    public static final Codec<ComponentEuStorage> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("energy_stored").forGetter(ComponentEuStorage::energyStored),
                    Codec.INT.fieldOf("energy_capacity").forGetter(ComponentEuStorage::energyCapacity)
            ).apply(builder, ComponentEuStorage::new)
    );
    public static final StreamCodec<ByteBuf, ComponentEuStorage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ComponentEuStorage::energyStored,
            ByteBufCodecs.INT,
            ComponentEuStorage::energyCapacity,
            ComponentEuStorage::new
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentEuStorage that)) return false;
        return energyStored == that.energyStored && energyCapacity == that.energyCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(energyStored, energyCapacity);
    }
}
