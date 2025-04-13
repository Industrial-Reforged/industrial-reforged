package com.indref.industrial_reforged.api.transportation;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.content.blocks.pipes.CableBlock;
import com.indref.industrial_reforged.networking.transportation.AddNextNodePayload;
import com.indref.industrial_reforged.networking.transportation.RemoveNextNodePayload;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NetworkNode<T> {
    private final TransportNetwork<T> network;
    private final BlockPos pos;
    private Map<Direction, NetworkNode<T>> next;
    private Map<Direction, BlockPos> uninitializedNext;
    private final Transporting<T> transporting;

    public NetworkNode(TransportNetwork<T> network, BlockPos pos) {
        this.network = network;
        this.pos = pos;
        this.next = new HashMap<>();
        this.transporting = new Transporting<>(network);
    }

    public NetworkNode(TransportNetwork<?> network, BlockPos pos, Map<Direction, BlockPos> next, Transporting<T> transporting) {
        this.network = (TransportNetwork<T>) network;
        this.pos = pos;
        this.uninitializedNext = next;
        this.transporting = transporting;
    }

    public void setChanged(ServerLevel level, NetworkNode<T> originNode, Direction changedDirection) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof CableBlock) {
            boolean connected = blockState.getValue(CableBlock.CONNECTION[changedDirection.get3DDataValue()]);
            BlockPos relative = pos.relative(changedDirection);
            if (connected) {
                if (this.network.hasNodeAt(level, relative)) {
                    NetworkNode<T> node = this.network.getNode(level, relative);
                    node.setChanged(level, originNode, changedDirection);
                    next.put(changedDirection, node);
                    this.network.setServerNodesChanged(level);
                    if (this.network.isSynced()) {
                        PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this.network, this.pos, changedDirection, relative));
                    }
                } else {
                    NetworkNode<T> nextNode = this.network.findNextNode(this, level, changedDirection);
                    if (nextNode != null) {
                        nextNode.setChanged(level, originNode, changedDirection);
                        next.put(changedDirection, nextNode);
                        this.network.setServerNodesChanged(level);
                        if (this.network.isSynced()) {
                            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this.network, this.pos, changedDirection, nextNode.pos));
                        }
                    } else {
                        NetworkNode<T> node = next.remove(changedDirection);
                        if (node != null) {
                            node.setChanged(level, originNode, changedDirection);
                            this.network.setServerNodesChanged(level);
                            if (this.network.isSynced()) {
                                PacketDistributor.sendToAllPlayers(new RemoveNextNodePayload(this.network, this.pos, changedDirection));
                            }
                        }
                    }
                }
            } else {
//                NetworkNode<T> nextNode = this.network.findNextNode(this, level, pos, changedDirection);
//                if (nextNode != null) {
//                    next.put(changedDirection, nextNode);
//                    this.network.setServerNodesChanged(level);
//                    if (this.network.isSynced()) {
//                        PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this.network, this.pos, changedDirection, nextNode.pos));
//                    }
//                } else {
//                    next.remove(changedDirection);
//                    this.network.setServerNodesChanged(level);
//                    if (this.network.isSynced()) {
//                        PacketDistributor.sendToAllPlayers(new RemoveNextNodePayload(this.network, this.pos, changedDirection));
//                    }
//                }
            }
        }
    }

    public void initialize(Map<BlockPos, NetworkNode<?>> nodes) {
        this.next = new HashMap<>();
        for (Map.Entry<Direction, BlockPos> entry : this.uninitializedNext.entrySet()) {
            this.next.put(entry.getKey(), (NetworkNode<T>) nodes.get(entry.getValue()));
        }
        this.uninitializedNext = null;
    }

    public TransportNetwork<T> getNetwork() {
        return network;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void removeNext(Direction direction) {
        this.next.remove(direction);
    }

    public void addNext(Direction direction, NetworkNode<?> node) {
        this.next.put(direction, (NetworkNode<T>) node);
    }

    public Map<Direction, NetworkNode<T>> getNext() {
        return next;
    }

    public Transporting<T> getTransporting() {
        return transporting;
    }

    public boolean uninitialized() {
        return uninitializedNext != null && next == null;
    }

    private Map<Direction, BlockPos> getNextAsPos() {
        return next.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().getPos()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    public static <T> Codec<NetworkNode<T>> codec(TransportNetwork<T> network) {
        return RecordCodecBuilder.create(inst -> inst.group(
                CodecUtils.registryCodec(IRRegistries.NETWORK).fieldOf("network").forGetter(NetworkNode::getNetwork),
                BlockPos.CODEC.fieldOf("pos").forGetter(NetworkNode::getPos),
                Codec.unboundedMap(StringRepresentable.fromEnum(Direction::values), BlockPos.CODEC).fieldOf("next").forGetter(NetworkNode::getNextAsPos),
                Transporting.codec(network.codec()).fieldOf("transporting").forGetter(NetworkNode::getTransporting)
        ).apply(inst, NetworkNode::new));
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, NetworkNode<T>> streamCodec(TransportNetwork<T> network) {
        return StreamCodec.composite(
                CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
                NetworkNode::getNetwork,
                BlockPos.STREAM_CODEC,
                NetworkNode::getPos,
                ByteBufCodecs.map(HashMap::new, CodecUtils.enumStreamCodec(Direction.class), BlockPos.STREAM_CODEC),
                NetworkNode::getNextAsPos,
                Transporting.streamCodec(network.streamCodec()),
                NetworkNode::getTransporting,
                NetworkNode::new
        );
    }

}
