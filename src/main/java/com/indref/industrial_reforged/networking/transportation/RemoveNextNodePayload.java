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
import org.jetbrains.annotations.NotNull;

public record RemoveNextNodePayload(TransportNetwork<?> network, BlockPos nodePos,
                                    Direction direction) implements CustomPacketPayload {
    public static final Type<RemoveNextNodePayload> TYPE = new Type<>(IndustrialReforged.rl("remove_next_node"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveNextNodePayload> STREAM_CODEC = StreamCodec.composite(
            CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
            RemoveNextNodePayload::network,
            BlockPos.STREAM_CODEC,
            RemoveNextNodePayload::nodePos,
            CodecUtils.enumStreamCodec(Direction.class),
            RemoveNextNodePayload::direction,
            RemoveNextNodePayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            NetworkNode<?> networkNode = ClientNodes.NODES.get(network).get(nodePos);
            if (networkNode != null) {
                networkNode.removeNext(direction);
            }
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle RemoveNextNodePayload");
            return null;
        });
    }
}
