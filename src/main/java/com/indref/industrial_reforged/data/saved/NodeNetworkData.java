package com.indref.industrial_reforged.data.saved;

import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.cache.NetworkRoute;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.api.transportation.cache.RouteCache;
import com.indref.industrial_reforged.util.IRCodecUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;

import java.util.*;

/** stores data for a single type of network
 * @param interactors TODO: Cache directions of interactor connections
 */
public record NodeNetworkData<T>(Map<BlockPos, NetworkNode<T>> nodes, Set<BlockPos> interactors, List<BlockPos> cacheOriginPositions) {
    public static <T> Codec<NodeNetworkData<T>> codec(TransportNetwork<T> network) {
        return RecordCodecBuilder.create(inst -> inst.group(
                Codec.unboundedMap(Codec.STRING, NetworkNode.codec(network)).fieldOf("nodes").forGetter(data -> IRCodecUtils.encodePosMap(data.nodes)),
                BlockPos.CODEC.listOf().fieldOf("interactors").forGetter(data -> List.copyOf(data.interactors)),
                BlockPos.CODEC.listOf().fieldOf("cache_origin_positions").forGetter(data -> data.cacheOriginPositions)
        ).apply(inst, NodeNetworkData::fromStringMap));
    }

    private static <T> NodeNetworkData<T> fromStringMap(Map<String, NetworkNode<T>> stringNetworkNodeMap, List<BlockPos> interactors, List<BlockPos> cacheOriginPositions) {
        return new NodeNetworkData<>(IRCodecUtils.decodePosMap(stringNetworkNodeMap), new HashSet<>(interactors), cacheOriginPositions);
    }

    public static <T> NodeNetworkData<T> empty() {
        return new NodeNetworkData<>(new HashMap<>(), new HashSet<>(), new ArrayList<>());
    }

}
