package com.indref.industrial_reforged.networking.transportation;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.client.transportation.ClientNodes;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

public record SyncInteractorPayload(TransportNetwork<?> network, Set<BlockPos> interactors) implements CustomPacketPayload {
    public static final Type<SyncInteractorPayload> TYPE = new Type<>(IndustrialReforged.rl("sync_interactor"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, SyncInteractorPayload> STREAM_CODEC = StreamCodec.composite(
            CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
            SyncInteractorPayload::network,
            ByteBufCodecs.collection(SyncInteractorPayload::newHashSet, BlockPos.STREAM_CODEC),
            SyncInteractorPayload::interactors,
            SyncInteractorPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientNodes.INTERACTORS.put(network, interactors);
        }).exceptionally(err -> {
           IndustrialReforged.LOGGER.error("Failed to handle SyncInteractor payload");
           return null;
        });
    }

    private static <T> Set<T> newHashSet(int capacity) {
        return new HashSet<>(capacity);
    }
}
