package com.indref.industrial_reforged.api.data.components;

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
public record EnergyStorage(int energyStored, int energyCapacity) {
    public static final EnergyStorage EMPTY = new EnergyStorage(0, 0);
    public static final Codec<EnergyStorage> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("energy_stored").forGetter(EnergyStorage::energyStored),
                    Codec.INT.fieldOf("energy_capacity").forGetter(EnergyStorage::energyCapacity)
            ).apply(builder, EnergyStorage::new)
    );
    public static final StreamCodec<ByteBuf, EnergyStorage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            EnergyStorage::energyStored,
            ByteBufCodecs.INT,
            EnergyStorage::energyCapacity,
            EnergyStorage::new
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnergyStorage that)) return false;
        return energyStored == that.energyStored && energyCapacity == that.energyCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(energyStored, energyCapacity);
    }
}
