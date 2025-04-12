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
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AddNextNodePayload(TransportNetwork<?> network, BlockPos nodePos, Direction direction,
                                 BlockPos nextPos) implements CustomPacketPayload {
    public static final Type<AddNextNodePayload> TYPE = new Type<>(IndustrialReforged.rl("add_next_node"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AddNextNodePayload> STREAM_CODEC = StreamCodec.composite(
            CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
            AddNextNodePayload::network,
            BlockPos.STREAM_CODEC,
            AddNextNodePayload::nodePos,
            CodecUtils.enumStreamCodec(Direction.class),
            AddNextNodePayload::direction,
            BlockPos.STREAM_CODEC,
            AddNextNodePayload::nextPos,
            AddNextNodePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            NetworkNode<?> networkNode = ClientNodes.NODES.get(network).get(nextPos);
            ClientNodes.NODES.get(network).get(nodePos).addNext(direction, networkNode);
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle AddNextNodePayload");
            return null;
        });
    }
}
