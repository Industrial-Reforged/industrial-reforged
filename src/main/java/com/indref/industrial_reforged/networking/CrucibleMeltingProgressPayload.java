package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CrucibleMeltingProgressPayload(BlockPos pos, int slotIndex, float progress) implements CustomPacketPayload {
    public static final Type<CrucibleMeltingProgressPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible_melting_progress_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrucibleMeltingProgressPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CrucibleMeltingProgressPayload::pos,
            ByteBufCodecs.INT,
            CrucibleMeltingProgressPayload::slotIndex,
            ByteBufCodecs.FLOAT,
            CrucibleMeltingProgressPayload::progress,
            CrucibleMeltingProgressPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
