package com.indref.industrial_reforged.networking.transportation;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.client.transportation.ClientNodes;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record SyncNetworkNodePayload<T>(TransportNetwork<T> network, HashMap<BlockPos, NetworkNode<T>> nodes) implements CustomPacketPayload {
    public static <T> SyncNetworkNodePayload<T> untyped(TransportNetwork<?> network, HashMap<BlockPos, NetworkNode<T>> nodes) {
        return new SyncNetworkNodePayload<>((TransportNetwork<T>) network, nodes);
    }

    public static <T> SyncNetworkNodePayload<T> untyped(TransportNetwork<?> network, Map<BlockPos, NetworkNode<T>> nodes) {
        return new SyncNetworkNodePayload<>((TransportNetwork<T>) network, new HashMap<>(nodes));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return type(network);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientNodes.NODES.computeIfAbsent(network, k -> new HashMap<>()).putAll(this.nodes);
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle sync network nodes payload", err);
            return null;
        });
    }

    public static <T> Type<SyncNetworkNodePayload<T>> type(TransportNetwork<?> network) {
        ResourceLocation key = IRRegistries.NETWORK.getKey(network);
        return new Type<>(ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "sync_%s_nodes".formatted(key.getPath())));
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, SyncNetworkNodePayload<T>> streamCodec(TransportNetwork<?> network) {
        return StreamCodec.composite(
                CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
                SyncNetworkNodePayload::network,
                ByteBufCodecs.map(HashMap::new, BlockPos.STREAM_CODEC, NetworkNode.streamCodec((TransportNetwork<T>) network)),
                SyncNetworkNodePayload::nodes,
                SyncNetworkNodePayload::untyped
        );
    }

}
