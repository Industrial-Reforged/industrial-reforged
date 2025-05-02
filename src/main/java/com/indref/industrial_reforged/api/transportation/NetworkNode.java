package com.indref.industrial_reforged.api.transportation;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.api.transportation.cache.NetworkRoute;
import com.indref.industrial_reforged.networking.transportation.AddNextNodePayload;
import com.indref.industrial_reforged.networking.transportation.RemoveNextNodePayload;
import com.indref.industrial_reforged.registries.IRNetworks;
import com.indref.industrial_reforged.translations.IRTranslations;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NetworkNode<T> {
    private final TransportNetwork<T> network;
    private final BlockPos pos;
    private Map<Direction, NetworkNode<T>> next;
    private Map<Direction, BlockPos> uninitializedNext;
    private final Transporting<T> transporting;
    private boolean dead;
    private Optional<Direction> interactorConnection;

    public NetworkNode(TransportNetwork<T> network, BlockPos pos) {
        this.network = network;
        this.pos = pos;
        this.next = new ConcurrentHashMap<>();
        this.transporting = new Transporting<>(network);
    }

    public NetworkNode(TransportNetwork<?> network, BlockPos pos, Map<Direction, BlockPos> next, Transporting<T> transporting, boolean dead, Optional<Direction> interactorConnection) {
        this.network = (TransportNetwork<T>) network;
        this.pos = pos;
        this.uninitializedNext = next;
        this.transporting = transporting;
        this.dead = dead;
        this.interactorConnection = interactorConnection;
    }

    public void initialize(Map<BlockPos, NetworkNode<?>> nodes) {
        this.next = new ConcurrentHashMap<>();
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

    public void setInteractorConnection(Direction interactorConnection) {
        this.interactorConnection = Optional.ofNullable(interactorConnection);
    }

    public Map<Direction, NetworkNode<T>> getNext() {
        return next;
    }

    public int getConnectionsAmount() {
        return getNext().size() + getInteractorConnectionsAmount();
    }

    public int getInteractorConnectionsAmount() {
        return interactorConnection != null ? 1 : 0;
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

    public Direction getInteractorConnection() {
        return interactorConnection.orElse(null);
    }

    public List<NetworkRoute<T>> getCachesReferencingThis(ServerLevel serverLevel) {
        List<NetworkRoute<T>> affectedRoutes = new ArrayList<>();
        Map<BlockPos, List<NetworkRoute<T>>> routes = this.network.getRouteCache(serverLevel).routes();
        for (List<NetworkRoute<T>> value : routes.values()) {
            for (NetworkRoute<T> route : value) {
                if (route.getPath().contains(this)) {
                    affectedRoutes.add(route);
                }
            }
        }
        return affectedRoutes;
    }

    // FIXME: This does not work at all, it always returns an empty map
    private Map<Direction, BlockPos> getNextAsPos() {
        if (next != null) {
            Map<Direction, NetworkNode<T>> snapshot = new HashMap<>(next);
            return snapshot.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().getPos()))
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        }
        return Collections.emptyMap();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NetworkNode<?> that)) return false;
        return dead == that.dead && Objects.equals(network, that.network) && Objects.equals(pos, that.pos) && Objects.equals(next, that.next) && Objects.equals(uninitializedNext, that.uninitializedNext) && Objects.equals(transporting, that.transporting) && interactorConnection == that.interactorConnection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(network, pos, transporting, dead, interactorConnection);
    }

    public static <T> Codec<NetworkNode<T>> codec(TransportNetwork<T> network) {
        return RecordCodecBuilder.create(inst -> inst.group(
                CodecUtils.registryCodec(IRRegistries.NETWORK).fieldOf("network").forGetter(NetworkNode::getNetwork),
                BlockPos.CODEC.fieldOf("pos").forGetter(NetworkNode::getPos),
                Codec.unboundedMap(StringRepresentable.fromEnum(Direction::values), BlockPos.CODEC).fieldOf("next").forGetter(NetworkNode::getNextAsPos),
                Transporting.codec(network.codec()).fieldOf("transporting").forGetter(NetworkNode::getTransporting),
                Codec.BOOL.fieldOf("dead").forGetter(NetworkNode::isDead),
                Direction.CODEC.optionalFieldOf("interactor").forGetter(node -> node.interactorConnection)
        ).apply(inst, NetworkNode::new));
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, NetworkNode<T>> streamCodec(TransportNetwork<T> network) {
        return StreamCodec.composite(
                CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
                tNetworkNode1 -> tNetworkNode1.getNetwork(),
                BlockPos.STREAM_CODEC,
                NetworkNode::getPos,
                ByteBufCodecs.map(HashMap::new, CodecUtils.enumStreamCodec(Direction.class), BlockPos.STREAM_CODEC),
                tNetworkNode -> tNetworkNode.getNextAsPos(),
                Transporting.streamCodec(network.streamCodec()),
                NetworkNode::getTransporting,
                ByteBufCodecs.BOOL,
                NetworkNode::isDead,
                ByteBufCodecs.optional(Direction.STREAM_CODEC),
                node -> node.interactorConnection,
                NetworkNode::new
        );
    }

}
