package com.indref.industrial_reforged.data.maps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CastingMoldValue(int capacity, boolean consumeCast) {
    public static final Codec<CastingMoldValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("capacity").forGetter(CastingMoldValue::capacity),
            Codec.BOOL.fieldOf("consume_cast").forGetter(CastingMoldValue::consumeCast)
    ).apply(instance, CastingMoldValue::new));
}
