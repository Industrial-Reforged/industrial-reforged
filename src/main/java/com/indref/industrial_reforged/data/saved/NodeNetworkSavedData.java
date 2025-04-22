package com.indref.industrial_reforged.data.saved;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class NodeNetworkSavedData extends SavedData {
    private static final Codec<Set<BlockPos>> INTERACTORS_CODEC = CodecUtils.set(BlockPos.CODEC);
    private final Map<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> networkNodes;
    private final Map<TransportNetwork<?>, Set<BlockPos>> interactors;

    public NodeNetworkSavedData(Map<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> networkNodes, Map<TransportNetwork<?>, Set<BlockPos>> interactors) {
        this.networkNodes = networkNodes;
        this.interactors = interactors;
    }

    public NodeNetworkSavedData() {
        this(new HashMap<>(), new HashMap<>());
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
        Map<TransportNetwork<?>, Set<BlockPos>> interactors = new HashMap<>();
        CompoundTag nodeNetworks = nbt.getCompound("node_networks");
        CompoundTag interactorsTag = nbt.getCompound("interactors");
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

        for (String key : interactorsTag.getAllKeys()) {
            TransportNetwork<?> network = IRRegistries.NETWORK.get(ResourceLocation.parse(key));
            Tag tag = interactorsTag.get(key);
            if (tag != null) {
                Optional<Set<BlockPos>> _interactors = INTERACTORS_CODEC.decode(NbtOps.INSTANCE, tag)
                        .resultOrPartial(err -> IndustrialReforged.LOGGER.error("Encountered error while decoding network interactors {}, {}", key, err))
                        .map(Pair::getFirst);
                _interactors.ifPresent(blockPos -> {
                    interactors.put(network, blockPos);
                });
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

        return new NodeNetworkSavedData(networkNodes, interactors);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag nodeNetworksNbt = new CompoundTag();
        CompoundTag interactorsNbt = new CompoundTag();
        for (Map.Entry<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> entry : this.networkNodes.entrySet()) {
            Map<String, NetworkNode<?>> value = entry.getValue().entrySet().stream()
                    .map(e -> new AbstractMap.SimpleEntry<>(String.valueOf(e.getKey().asLong()), e.getValue()))
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
            Codec<Map<String, NetworkNode<?>>> networkeCodec = networkCodec(entry.getKey());
            networkeCodec.encodeStart(NbtOps.INSTANCE, value)
                    .resultOrPartial(err -> IndustrialReforged.LOGGER.error("Encountered error while encoding node network {}, {}", IRRegistries.NETWORK.getKey(entry.getKey()), err))
                    .ifPresent(tag1 -> {
                nodeNetworksNbt.put(IRRegistries.NETWORK.getKey(entry.getKey()).toString(), tag1);
            });
        }

        for (Map.Entry<TransportNetwork<?>, Set<BlockPos>> entry : this.interactors.entrySet()) {
            INTERACTORS_CODEC.encodeStart(NbtOps.INSTANCE, entry.getValue())
                    .resultOrPartial(err -> IndustrialReforged.LOGGER.error("Encountered error while encoding network interactors {}, {}", IRRegistries.NETWORK.getKey(entry.getKey()), err))
                    .ifPresent(tag1 -> {
                        interactorsNbt.put(IRRegistries.NETWORK.getKey(entry.getKey()).toString(), tag1);
                    });
        }

        tag.put("node_networks", nodeNetworksNbt);
        tag.put("interactors", interactorsNbt);
        return tag;
    }

    private static Codec<Map<String, NetworkNode<?>>> networkCodec(TransportNetwork<?> network) {
        Codec<? extends NetworkNode<?>> codec = NetworkNode.codec((TransportNetwork<?>) network);
        Codec<? extends Map<String, ? extends NetworkNode<?>>> listCodec = Codec.unboundedMap(Codec.STRING, codec);
        return (Codec<Map<String, NetworkNode<?>>>) listCodec;
    }
}
