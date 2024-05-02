package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CastingDurationPayload(int duration, int maxDuration, BlockPos blockPos) implements CustomPacketPayload {
    public static final Type<CastingDurationPayload> TYPE = new Type<>(new ResourceLocation(IndustrialReforged.MODID, "casting_duration_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CastingDurationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            CastingDurationPayload::duration,
            ByteBufCodecs.INT,
            CastingDurationPayload::maxDuration,
            BlockPos.STREAM_CODEC,
            CastingDurationPayload::blockPos,
            CastingDurationPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
