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
public record ComponentEnergyStorage(int energyStored, int energyCapacity) {
    public static final ComponentEnergyStorage EMPTY = new ComponentEnergyStorage(0, 0);
    public static final Codec<ComponentEnergyStorage> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("energy_stored").forGetter(ComponentEnergyStorage::energyStored),
                    Codec.INT.fieldOf("energy_capacity").forGetter(ComponentEnergyStorage::energyCapacity)
            ).apply(builder, ComponentEnergyStorage::new)
    );
    public static final StreamCodec<ByteBuf, ComponentEnergyStorage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ComponentEnergyStorage::energyStored,
            ByteBufCodecs.INT,
            ComponentEnergyStorage::energyCapacity,
            ComponentEnergyStorage::new
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentEnergyStorage that)) return false;
        return energyStored == that.energyStored && energyCapacity == that.energyCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(energyStored, energyCapacity);
    }
}
