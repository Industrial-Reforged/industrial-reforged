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

public record AddInteractorPayload(TransportNetwork<?> network, BlockPos interactorPos) implements CustomPacketPayload {
    public static final Type<AddInteractorPayload> TYPE = new Type<>(IndustrialReforged.rl("add_interactor"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, AddInteractorPayload> STREAM_CODEC = StreamCodec.composite(
            CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
            AddInteractorPayload::network,
            BlockPos.STREAM_CODEC,
            AddInteractorPayload::interactorPos,
            AddInteractorPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientNodes.INTERACTORS.computeIfAbsent(network, k -> new HashSet<>()).add(interactorPos);
        }).exceptionally(err -> {
           IndustrialReforged.LOGGER.error("Failed to handle AddInteractor payload", err);
           return null;
        });
    }

}
