package com.indref.industrial_reforged.api.transportation;

import com.indref.industrial_reforged.IRRegistries;
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
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

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
    private boolean dead;

    public NetworkNode(TransportNetwork<T> network, BlockPos pos) {
        this.network = network;
        this.pos = pos;
        this.next = new HashMap<>();
        this.transporting = new Transporting<>(network);
    }

    public NetworkNode(TransportNetwork<?> network, BlockPos pos, Map<Direction, BlockPos> next, Transporting<T> transporting, boolean dead) {
        this.network = (TransportNetwork<T>) network;
        this.pos = pos;
        this.uninitializedNext = next;
        this.transporting = transporting;
        this.dead = dead;
    }

    public void setChanged(ServerLevel level, @Nullable NetworkNode<T> originNode, Direction changedDirection) {
//        BlockState blockState = level.getBlockState(pos);
//        if (blockState.getBlock() instanceof CableBlock) {
//            boolean connected = blockState.getValue(CableBlock.CONNECTION[changedDirection.get3DDataValue()]);
//            BlockPos relative = pos.relative(changedDirection);
//            if (connected) {
//                if (this.network.hasNodeAt(level, relative)) {
//                    NetworkNode<T> nextNode = this.network.getNode(level, relative);
//                    if (!nextNode.isDead()) {
//                        next.put(changedDirection, nextNode);
//                        nextNode.getNext().put(changedDirection.getOpposite(), this);
//                        this.network.setServerNodesChanged(level);
//                        if (this.network.isSynced()) {
//                            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this.network, this.pos, changedDirection, relative));
//                            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this.network, relative, changedDirection, this.pos));
//                        }
//                    }
//                } else {
//                    NetworkNode<T> nextNode = this.network.findNextNode(this, level, pos, changedDirection);
//                    if (nextNode != null && !nextNode.isDead()) {
//                        next.put(changedDirection, nextNode);
//                        nextNode.getNext().put(changedDirection.getOpposite(), this);
//                        this.network.setServerNodesChanged(level);
//                        if (this.network.isSynced()) {
//                            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this.network, this.pos, changedDirection, nextNode.pos));
//                            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this.network, nextNode.pos, changedDirection, this.pos));
//                        }
//                    } else {
//                        next.remove(changedDirection);
//                        this.network.setServerNodesChanged(level);
//                        if (this.network.isSynced()) {
//                            PacketDistributor.sendToAllPlayers(new RemoveNextNodePayload(this.network, this.pos, changedDirection));
//                        }
//                    }
//                }
//            }
//        }
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

    public void addNextNodeSynced(Direction direction, NetworkNode<T> nextNode) {
        this.next.put(direction, nextNode);
        if (this.network.isSynced()) {
            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this.network, this.pos, direction, nextNode.getPos()));
        }
    }

    public void removeNextNodeSynced(Direction direction) {
        this.next.remove(direction);
        if (this.network.isSynced()) {
            PacketDistributor.sendToAllPlayers(new RemoveNextNodePayload(this.network, this.pos, direction));
        }
    }

    public void onNextNodeAdded(NetworkNode<T> originNode, Direction originNodeDirection) {
        this.addNextNodeSynced(originNodeDirection, originNode);
    }

    public void onConnectionAdded(ServerLevel serverLevel, BlockPos updatedPos, Direction updatedPosDirection) {
        NetworkNode<T> nextNode = this.network.findNextNode(null, serverLevel, this.pos, updatedPosDirection);
        if (nextNode != null && !nextNode.isDead()) {
            this.addNextNodeSynced(updatedPosDirection, nextNode);
        }
    }

    public void onConnectionRemoved(ServerLevel serverLevel, BlockPos updatedPos, Direction updatedPosDirection) {
        NetworkNode<T> nextNode = this.network.findNextNode(null, serverLevel, this.pos, updatedPosDirection);
        if (nextNode != null) {
            this.removeNextNodeSynced(updatedPosDirection);
        }
    }

    public void addNext(Direction direction, NetworkNode<?> node) {
        this.next.put(direction, (NetworkNode<T>) node);
    }

    public void setDead(boolean dead) {
        this.dead = dead;
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

    public boolean isDead() {
        return dead;
    }

    private Map<Direction, BlockPos> getNextAsPos() {
        Map<Direction, NetworkNode> snapshot = new HashMap<>(next);
        return snapshot.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().getPos()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    public static <T> Codec<NetworkNode<T>> codec(TransportNetwork<T> network) {
        return RecordCodecBuilder.create(inst -> inst.group(
                CodecUtils.registryCodec(IRRegistries.NETWORK).fieldOf("network").forGetter(NetworkNode::getNetwork),
                BlockPos.CODEC.fieldOf("pos").forGetter(NetworkNode::getPos),
                Codec.unboundedMap(StringRepresentable.fromEnum(Direction::values), BlockPos.CODEC).fieldOf("next").forGetter(NetworkNode::getNextAsPos),
                Transporting.codec(network.codec()).fieldOf("transporting").forGetter(NetworkNode::getTransporting),
                Codec.BOOL.fieldOf("dead").forGetter(NetworkNode::isDead)
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
                ByteBufCodecs.BOOL,
                NetworkNode::isDead,
                NetworkNode::new
        );
    }

}
