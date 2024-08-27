package com.indref.industrial_reforged.registries.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record ComponentTapeMeasure(@Nullable BlockPos firstPos, boolean tapeMeasureExtended) {
    public static final ComponentTapeMeasure EMPTY = new ComponentTapeMeasure(BlockPos.ZERO, false);
    public static final Codec<ComponentTapeMeasure> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    BlockPos.CODEC.fieldOf("first_pos").forGetter(ComponentTapeMeasure::firstPos),
                    Codec.BOOL.fieldOf("tape_measure_extended").forGetter(ComponentTapeMeasure::tapeMeasureExtended)
            ).apply(builder, ComponentTapeMeasure::new)
    );
    public static final StreamCodec<ByteBuf, ComponentTapeMeasure> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ComponentTapeMeasure decode(ByteBuf byteBuf) {
            return new ComponentTapeMeasure(BlockPos.STREAM_CODEC.decode(byteBuf), byteBuf.readBoolean());
        }

        @Override
        public void encode(ByteBuf byteBuf, ComponentTapeMeasure tmd) {
            BlockPos.STREAM_CODEC.encode(byteBuf, tmd.firstPos);
            byteBuf.writeBoolean(tmd.tapeMeasureExtended);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentTapeMeasure that)) return false;
        return tapeMeasureExtended == that.tapeMeasureExtended && Objects.equals(firstPos, that.firstPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstPos, tapeMeasureExtended);
    }
}
