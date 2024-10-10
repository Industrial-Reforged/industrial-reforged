package com.indref.industrial_reforged.api.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

public enum HorizontalDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static final Codec<HorizontalDirection> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("direction").forGetter(HorizontalDirection::ordinal)
    ).apply(instance, i -> HorizontalDirection.values()[i]));
    public static final StreamCodec<RegistryFriendlyByteBuf, HorizontalDirection> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            HorizontalDirection::ordinal,
            i -> HorizontalDirection.values()[i]
    );

    public Direction toRegularDirection() {
        return switch (this) {
            case NORTH -> Direction.NORTH;
            case EAST -> Direction.EAST;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
        };
    }

    public static @Nullable HorizontalDirection fromRegularDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> null;
        };
    }
}