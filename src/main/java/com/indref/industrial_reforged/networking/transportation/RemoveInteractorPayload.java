package com.indref.industrial_reforged.networking.transportation;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.client.transportation.ClientNodes;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;

public record RemoveInteractorPayload(TransportNetwork<?> network, BlockPos interactorPos) implements CustomPacketPayload {
    public static final Type<RemoveInteractorPayload> TYPE = new Type<>(IndustrialReforged.rl("remove_interactor"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, RemoveInteractorPayload> STREAM_CODEC = StreamCodec.composite(
            CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
            RemoveInteractorPayload::network,
            BlockPos.STREAM_CODEC,
            RemoveInteractorPayload::interactorPos,
            RemoveInteractorPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientNodes.INTERACTORS.computeIfAbsent(network, k -> new HashSet<>()).remove(interactorPos);
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle RemoveInteractor payload", err);
            return null;
        });
    }

}