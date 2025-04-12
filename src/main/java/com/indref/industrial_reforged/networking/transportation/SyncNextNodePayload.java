package com.indref.industrial_reforged.networking.transportation;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.client.transportation.ClientNodes;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record SyncNextNodePayload(TransportNetwork<?> network) implements CustomPacketPayload {
    public static final Type<SyncNextNodePayload> TYPE = new Type<>(IndustrialReforged.rl("sync_next_nodes"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncNextNodePayload> STREAM_CODEC = StreamCodec.composite(
            CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
            SyncNextNodePayload::network,
            SyncNextNodePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Map<BlockPos, NetworkNode<?>> nodes = ClientNodes.NODES.get(network);
            for (NetworkNode<?> node : nodes.values()) {
                if (node.uninitialized()) {
                    node.initialize(nodes);
                }
            }
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle RemoveNextNodePayload");
            return null;
        });
    }
}
