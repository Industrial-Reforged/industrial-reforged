package com.indref.industrial_reforged.data.saved;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class NodeNetworkSavedData extends SavedData {
    public static final Codec<NodeNetworkSavedData> CODEC = Codec.unit(NodeNetworkSavedData::new);
    private final Map<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> networkNodes;

    public NodeNetworkSavedData(Map<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> networkNodes) {
        this.networkNodes = networkNodes;
    }

    public NodeNetworkSavedData() {
        this(new HashMap<>());
    }

    public static NodeNetworkSavedData getNetworks(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(factory(), IndustrialReforged.rl("node_networks").toString());
    }

    public Map<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> getNetworkNodes() {
        return networkNodes;
    }

    private static SavedData.Factory<NodeNetworkSavedData> factory() {
        return new SavedData.Factory<>(NodeNetworkSavedData::new, NodeNetworkSavedData::load);
    }

    private static NodeNetworkSavedData load(CompoundTag nbt, HolderLookup.Provider lookup) {
        Map<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> networkNodes = new HashMap<>();
        CompoundTag nodeNetworks = nbt.getCompound("node_networks");
        for (String key : nodeNetworks.getAllKeys()) {
            TransportNetwork<?> network = IRRegistries.NETWORK.get(ResourceLocation.parse(key));
            Codec<Map<String, NetworkNode<?>>> listCodec = networkCodec(network);
            Tag tag = nodeNetworks.get(key);
            if (tag != null) {
                Optional<Map<String, NetworkNode<?>>> nodes = listCodec.decode(NbtOps.INSTANCE, tag)
                        .resultOrPartial(err -> IndustrialReforged.LOGGER.error("Encountered error while decoding node network {}, {}", key, err))
                        .map(Pair::getFirst);
                if (nodes.isPresent()) {
                    Map<BlockPos, NetworkNode<?>> nodes1 = nodes.get().entrySet().stream()
                            .map(e -> new AbstractMap.SimpleEntry<>(BlockPos.of(Long.parseLong(e.getKey())), e.getValue()))
                            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
                    networkNodes.put(network, nodes1);
                }
            }
        }

        for (Map.Entry<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> entry : networkNodes.entrySet()) {
            for (Map.Entry<BlockPos, NetworkNode<?>> innerEntry : entry.getValue().entrySet()) {
                NetworkNode<?> node = innerEntry.getValue();
                if (node.uninitialized()) {
                    node.initialize(entry.getValue());
                }
            }
        }

        return new NodeNetworkSavedData(networkNodes);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        for (Map.Entry<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> entry : this.networkNodes.entrySet()) {
            Map<String, NetworkNode<?>> value = entry.getValue().entrySet().stream()
                    .map(e -> new AbstractMap.SimpleEntry<>(String.valueOf(e.getKey().asLong()), e.getValue()))
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
            DataResult<Tag> nodesTag = networkCodec(entry.getKey()).encodeStart(NbtOps.INSTANCE, value);
            nodesTag.resultOrPartial(err -> IndustrialReforged.LOGGER.error("Encountered error while encoding node network {}, {}", IRRegistries.NETWORK.getKey(entry.getKey()), err)).ifPresent(tag1 -> {
                nbt.put(IRRegistries.NETWORK.getKey(entry.getKey()).toString(), tag1);
            });
        }
        tag.put("node_networks", nbt);
        return tag;
    }

    private static Codec<Map<String, NetworkNode<?>>> networkCodec(TransportNetwork<?> network) {
        Codec<? extends NetworkNode<?>> codec = NetworkNode.codec((TransportNetwork<?>) network);
        Codec<? extends Map<String, ? extends NetworkNode<?>>> listCodec = Codec.unboundedMap(Codec.STRING, codec);
        return (Codec<Map<String, NetworkNode<?>>>) listCodec;
    }
}
