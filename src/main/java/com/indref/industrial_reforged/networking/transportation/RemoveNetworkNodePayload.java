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

public record RemoveNetworkNodePayload<T>(TransportNetwork<T> network, BlockPos pos) implements CustomPacketPayload {
    private static <T> RemoveNetworkNodePayload<T> untyped(TransportNetwork<?> network, BlockPos pos) {
        return new RemoveNetworkNodePayload<>((TransportNetwork<T>) network, pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return type(network);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientNodes.NODES.get(network).remove(pos);
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle remove network node payload", err);
            return null;
        });
    }

    public static <T> Type<RemoveNetworkNodePayload<T>> type(TransportNetwork<?> network) {
        ResourceLocation key = IRRegistries.NETWORK.getKey(network);
        return new Type<>(ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "remove_%s_node".formatted(key.getPath())));
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, RemoveNetworkNodePayload<T>> streamCodec(TransportNetwork<?> network) {
        return StreamCodec.composite(
                CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
                RemoveNetworkNodePayload::network,
                BlockPos.STREAM_CODEC,
                RemoveNetworkNodePayload::pos,
                RemoveNetworkNodePayload::untyped
        );
    }

}
