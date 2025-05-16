package com.indref.industrial_reforged.data.saved;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NodeNetworkSavedData extends SavedData {
    private static final SavedData.Factory<NodeNetworkSavedData> FACTORY = new Factory<>(NodeNetworkSavedData::new, NodeNetworkSavedData::load);
    private final Map<TransportNetwork<?>, NodeNetworkData<?>> data;

    public NodeNetworkSavedData(Map<TransportNetwork<?>, NodeNetworkData<?>> data) {
        this.data = data;
    }

    public NodeNetworkSavedData() {
        this(new HashMap<>());
    }

    public Map<TransportNetwork<?>, NodeNetworkData<?>> getData() {
        return data;
    }

    public static NodeNetworkSavedData getNetworkData(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(FACTORY, IndustrialReforged.rl("node_networks").toString());
    }

    private static NodeNetworkSavedData load(CompoundTag tag, HolderLookup.Provider lookup) {
        CompoundTag nbt = tag.getCompound("node_network");
        Map<TransportNetwork<?>, NodeNetworkData<?>> data = new HashMap<>();
        for (String key : nbt.getAllKeys()) {
            TransportNetwork<?> network = IRRegistries.NETWORK.get(ResourceLocation.parse(key));
            NodeNetworkData<?> networkData = decodeData(network, nbt.get(key));
            data.put(network, networkData);
        }
        for (Map.Entry<TransportNetwork<?>, NodeNetworkData<?>> entry : data.entrySet()) {
            for (NetworkNode<?> node : entry.getValue().nodes().values()) {
                if (node.uninitialized()) {
                    node.initialize(data.getOrDefault(entry.getKey(), NodeNetworkData.empty()).nodes());
                }
            }
        }
        return new NodeNetworkSavedData(data);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();

        for (Map.Entry<TransportNetwork<?>, NodeNetworkData<?>> entry : this.data.entrySet()) {
            Tag tag1 = encodeData(entry.getKey(), entry.getValue());
            if (tag1 != null) {
                nbt.put(IRRegistries.NETWORK.getKey(entry.getKey()).toString(), tag1);
            }
        }

        tag.put("node_network", nbt);
        return tag;
    }

    private static <T> NodeNetworkData<T> decodeData(TransportNetwork<?> key, Tag tag) {
        TransportNetwork<T> network = (TransportNetwork<T>) key;
        return NodeNetworkData.codec(network).decode(NbtOps.INSTANCE, tag).resultOrPartial(err -> IndustrialReforged.LOGGER.error("Failed to decode node network data: {}", err)).map(Pair::getFirst).orElse(null);
    }

    private static <T> Tag encodeData(TransportNetwork<?> key, NodeNetworkData<?> value) {
        TransportNetwork<T> network = (TransportNetwork<T>) key;
        NodeNetworkData<T> data = (NodeNetworkData<T>) value;
        Optional<Tag> tag = NodeNetworkData.codec(network)
                .encodeStart(NbtOps.INSTANCE, data)
                .resultOrPartial(err -> IndustrialReforged.LOGGER.error("Failed to encode node network data: {}", err));
        return tag.orElse(null);
    }
}
