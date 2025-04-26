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
 * @param routeCache TODO: We only want to encode a set of block positions rather than all of the nodes
 */
public record NodeNetworkData<T>(Map<BlockPos, NetworkNode<T>> nodes, Set<BlockPos> interactors, RouteCache<T> routeCache) {
    public static <T> Codec<NodeNetworkData<T>> codec(TransportNetwork<T> network) {
        return RecordCodecBuilder.create(inst -> inst.group(
                Codec.unboundedMap(Codec.STRING, NetworkNode.codec(network)).fieldOf("nodes").forGetter(data -> IRCodecUtils.encodePosMap(data.nodes)),
                BlockPos.CODEC.listOf().fieldOf("interactors").forGetter(data -> List.copyOf(data.interactors)),
                RouteCache.codec(network).fieldOf("route_cache").forGetter(data -> data.routeCache)
        ).apply(inst, NodeNetworkData::fromStringMap));
    }

    private static <T> NodeNetworkData<T> fromStringMap(Map<String, NetworkNode<T>> stringNetworkNodeMap, List<BlockPos> interactors, RouteCache<T> routeCache) {
        return new NodeNetworkData<>(IRCodecUtils.decodePosMap(stringNetworkNodeMap), new HashSet<>(interactors), routeCache);
    }

    public static <T> NodeNetworkData<T> empty() {
        return new NodeNetworkData<>(new HashMap<>(), new HashSet<>(), new RouteCache<>(new HashMap<>()));
    }

}
