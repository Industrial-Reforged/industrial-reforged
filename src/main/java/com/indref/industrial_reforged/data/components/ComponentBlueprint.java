package com.indref.industrial_reforged.data.components;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.util.RegistryUtils;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record ComponentBlueprint(BlockPos controllerPos, HorizontalDirection direction, Multiblock multiblock) {
    public static final ComponentBlueprint EMPTY = new ComponentBlueprint(null, null, null);

    public static final Codec<ComponentBlueprint> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BlockPos.CODEC.fieldOf("controllerPos").forGetter(ComponentBlueprint::controllerPos),
            RegistryUtils.enumCodec(HorizontalDirection.class).fieldOf("direction").forGetter(ComponentBlueprint::direction),
            RegistryUtils.registryCodec(IRRegistries.MULTIBLOCK).fieldOf("multiblock").forGetter(ComponentBlueprint::multiblock)
    ).apply(builder, ComponentBlueprint::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentBlueprint> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ComponentBlueprint::controllerPos,
            RegistryUtils.enumStreamCodec(HorizontalDirection.class),
            ComponentBlueprint::direction,
            RegistryUtils.registryStreamCodec(IRRegistries.MULTIBLOCK),
            ComponentBlueprint::multiblock,
            ComponentBlueprint::new
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentBlueprint(BlockPos pos, HorizontalDirection direction1, Multiblock multiblock1))) return false;
        return Objects.equals(multiblock, multiblock1) && Objects.equals(controllerPos, pos) && direction == direction1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(controllerPos, direction, multiblock);
    }
}
