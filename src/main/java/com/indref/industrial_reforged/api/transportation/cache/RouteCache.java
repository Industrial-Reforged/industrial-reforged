package com.indref.industrial_reforged.api.transportation.cache;

import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.util.IRCodecUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public record RouteCache<T>(Map<BlockPos, List<NetworkRoute<T>>> routes) {
    public static <T> Codec<RouteCache<T>> codec(TransportNetwork<T> network) {
        return RecordCodecBuilder.create(inst -> inst.group(
                Codec.unboundedMap(Codec.STRING, NetworkRoute.codec(network).listOf()).fieldOf("routes").forGetter(cache -> IRCodecUtils.encodePosMap(cache.routes))
        ).apply(inst, routes -> new RouteCache<>(IRCodecUtils.decodePosMap(routes))));
    }

    public List<NetworkRoute<T>> computeIfAbsent(BlockPos key, Function<BlockPos, List<NetworkRoute<T>>> mappingFunction) {
        return this.routes.computeIfAbsent(key, mappingFunction);
    }

}
