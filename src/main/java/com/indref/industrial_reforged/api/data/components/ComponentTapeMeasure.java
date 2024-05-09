package com.indref.industrial_reforged.api.data.components;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ComponentTapeMeasure(BlockPos firstPos, boolean tape_measure_extended) {
    public static final ComponentTapeMeasure EMPTY = new ComponentTapeMeasure(BlockPos.ZERO, false);
    public static final Codec<ComponentTapeMeasure> CODEC = Codec.pair(BlockPos.CODEC, Codec.BOOL)
            .xmap(pair -> new ComponentTapeMeasure(pair.getFirst(), pair.getSecond()), es -> new Pair<>(es.firstPos, es.tape_measure_extended));
    public static final StreamCodec<ByteBuf, ComponentTapeMeasure> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ComponentTapeMeasure decode(ByteBuf byteBuf) {
            return new ComponentTapeMeasure(BlockPos.STREAM_CODEC.decode(byteBuf), byteBuf.readBoolean());
        }

        @Override
        public void encode(ByteBuf byteBuf, ComponentTapeMeasure tmd) {
            BlockPos.STREAM_CODEC.encode(byteBuf, tmd.firstPos);
            byteBuf.writeBoolean(tmd.tape_measure_extended);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentTapeMeasure that)) return false;
        return tape_measure_extended == that.tape_measure_extended && Objects.equals(firstPos, that.firstPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstPos, tape_measure_extended);
    }
}
