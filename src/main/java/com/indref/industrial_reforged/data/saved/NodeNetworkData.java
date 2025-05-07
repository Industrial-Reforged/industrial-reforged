package com.indref.industrial_reforged.data.saved;

import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.util.IRCodecUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

import java.util.*;

/** stores data for a single type of network
 * @param interactors TODO: Cache directions of interactor connections
 */
public record NodeNetworkData<T>(Map<BlockPos, NetworkNode<T>> nodes, Set<BlockPos> interactors) {
    public static <T> Codec<NodeNetworkData<T>> codec(TransportNetwork<T> network) {
        return RecordCodecBuilder.create(inst -> inst.group(
                Codec.unboundedMap(Codec.STRING, NetworkNode.codec(network)).fieldOf("nodes").forGetter(data -> IRCodecUtils.encodePosMap(data.nodes)),
                BlockPos.CODEC.listOf().fieldOf("interactors").forGetter(data -> List.copyOf(data.interactors))
        ).apply(inst, NodeNetworkData::codecNew));
    }

    private static <T> NodeNetworkData<T> codecNew(Map<String, NetworkNode<T>> stringNetworkNodeMap, List<BlockPos> interactors) {
        return new NodeNetworkData<>(IRCodecUtils.decodePosMap(stringNetworkNodeMap), new HashSet<>(interactors));
    }

    public static <T> NodeNetworkData<T> empty() {
        return new NodeNetworkData<>(new HashMap<>(), new HashSet<>());
    }

}
