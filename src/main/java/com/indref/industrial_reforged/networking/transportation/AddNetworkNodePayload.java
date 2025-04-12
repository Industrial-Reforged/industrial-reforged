package com.indref.industrial_reforged.networking.transportation;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.client.transportation.ClientNodes;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AddNetworkNodePayload<T>(TransportNetwork<T> network, BlockPos pos,
                                       NetworkNode<T> node) implements CustomPacketPayload {
    private static <T> AddNetworkNodePayload<T> untyped(TransportNetwork<?> network, BlockPos pos, NetworkNode<?> tNetworkNode) {
        return new AddNetworkNodePayload<>((TransportNetwork<T>) network, pos, (NetworkNode<T>) tNetworkNode);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return type(network);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (node.uninitialized()) {
                node.initialize(ClientNodes.NODES.get(network));
            }
            ClientNodes.NODES.get(network).put(pos, node);
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle add network node payload", err);
            return null;
        });
    }

    public static <T> Type<AddNetworkNodePayload<T>> type(TransportNetwork<?> network) {
        ResourceLocation key = IRRegistries.NETWORK.getKey(network);
        return new Type<>(ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "add_%s_node".formatted(key.getPath())));
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, AddNetworkNodePayload<T>> streamCodec(TransportNetwork<?> network) {
        return StreamCodec.composite(
                CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
                AddNetworkNodePayload::network,
                BlockPos.STREAM_CODEC,
                AddNetworkNodePayload::pos,
                NetworkNode.streamCodec((TransportNetwork<T>) network),
                AddNetworkNodePayload::node,
                AddNetworkNodePayload::untyped
        );
    }

}
