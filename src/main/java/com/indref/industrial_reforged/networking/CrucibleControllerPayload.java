package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CrucibleControllerPayload(BlockPos wallPos, BlockPos controllerPos) implements CustomPacketPayload {
    public static final Type<CrucibleControllerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible_controller_sync_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrucibleControllerPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CrucibleControllerPayload::wallPos,
            BlockPos.STREAM_CODEC,
            CrucibleControllerPayload::controllerPos,
            CrucibleControllerPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
