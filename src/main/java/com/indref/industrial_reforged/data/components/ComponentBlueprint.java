package com.indref.industrial_reforged.data.components;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record ComponentBlueprint(BlockPos controllerPos, HorizontalDirection direction, Multiblock multiblock) {
    public static final ComponentBlueprint EMPTY = new ComponentBlueprint(null, null, null);

    public static final Codec<ComponentBlueprint> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BlockPos.CODEC.fieldOf("controllerPos").forGetter(ComponentBlueprint::controllerPos),
            HorizontalDirection.CODEC.fieldOf("direction").forGetter(ComponentBlueprint::direction),
            Multiblock.CODEC.fieldOf("multiblock").forGetter(ComponentBlueprint::multiblock)
    ).apply(builder, ComponentBlueprint::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentBlueprint> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ComponentBlueprint::controllerPos,
            HorizontalDirection.STREAM_CODEC,
            ComponentBlueprint::direction,
            Multiblock.STREAM_CODEC,
            ComponentBlueprint::multiblock,
            ComponentBlueprint::new
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentBlueprint that)) return false;
        return Objects.equals(multiblock, that.multiblock) && Objects.equals(controllerPos, that.controllerPos) && direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(controllerPos, direction, multiblock);
    }
}
