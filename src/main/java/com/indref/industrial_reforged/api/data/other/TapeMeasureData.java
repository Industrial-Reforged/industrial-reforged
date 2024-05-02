package com.indref.industrial_reforged.api.data.other;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public record TapeMeasureData(BlockPos firstPos, boolean tape_measure_extended) {
    private static final Codec<Pair<BlockPos, Boolean>> PAIR_CODEC =
            Codec.pair(BlockPos.CODEC, Codec.BOOL);
    public static final Codec<TapeMeasureData> CODEC =
            PAIR_CODEC.xmap(pair -> new TapeMeasureData(pair.getFirst(), pair.getSecond()), es -> new Pair<>(es.firstPos, es.tape_measure_extended));
    public static final StreamCodec<ByteBuf, TapeMeasureData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull TapeMeasureData decode(ByteBuf byteBuf) {
            return new TapeMeasureData(BlockPos.STREAM_CODEC.decode(byteBuf), byteBuf.readBoolean());
        }

        @Override
        public void encode(ByteBuf byteBuf, TapeMeasureData tmd) {
            BlockPos.STREAM_CODEC.encode(byteBuf, tmd.firstPos);
            byteBuf.writeBoolean(tmd.tape_measure_extended);
        }
    };
}
