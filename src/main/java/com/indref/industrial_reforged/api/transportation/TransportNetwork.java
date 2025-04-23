package com.indref.industrial_reforged.api.transportation;

import com.indref.industrial_reforged.data.saved.NodeNetworkSavedData;
import com.indref.industrial_reforged.networking.transportation.AddInteractorPayload;
import com.indref.industrial_reforged.networking.transportation.AddNetworkNodePayload;
import com.indref.industrial_reforged.networking.transportation.RemoveInteractorPayload;
import com.indref.industrial_reforged.networking.transportation.RemoveNetworkNodePayload;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriPredicate;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TransportNetwork<T> {
    private final BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> nodeFactory;
    private final Codec<T> transportingCodec;
    private final TransportingHandler<T> transportingHandler;
    private final BiFunction<ServerLevel, NetworkNode<T>, Float> lossPerBlockFunction;
    private final BiFunction<ServerLevel, NetworkNode<T>, Float> transferSpeedFunction;
    private final TriPredicate<Level, BlockPos, Direction> interactorCheckFunction;
    private final int maxConnectionDistance;
    private final StreamCodec<ByteBuf, T> streamCodec;

    private TransportNetwork(Builder<T> builder) {
        this.nodeFactory = builder.nodeFactory;
        this.transportingCodec = builder.transportingCodec;
        this.transportingHandler = builder.transportingHandler;
        this.lossPerBlockFunction = builder.lossPerBlockFunction;
        this.transferSpeedFunction = builder.transferSpeedFunction;
        this.interactorCheckFunction = builder.interactorCheckFunction;
        this.maxConnectionDistance = builder.maxConnectionDistance;
        this.streamCodec = builder.streamCodec;
    }

    public NetworkNode<T> createNode(BlockPos pos) {
        return this.nodeFactory.apply(this, pos);
    }

    public void addConnection(ServerLevel serverLevel, BlockPos pos, Direction direction0, Direction direction1) {
        NetworkNode<T> node0 = this.findNextNode(null, serverLevel, pos, direction0);
        if (node0 != null && !node0.isDead()) {
            node0.onConnectionAdded(serverLevel, pos, direction0.getOpposite());
        }
        NetworkNode<T> node1 = this.findNextNode(null, serverLevel, pos, direction1);
        if (node1 != null && !node1.isDead()) {
            node1.onConnectionAdded(serverLevel, pos, direction1.getOpposite());
        }
    }

    public void removeConnection(ServerLevel serverLevel, BlockPos pos, Direction direction0, Direction direction1) {
        NetworkNode<T> node0 = this.findNextNode(null, serverLevel, pos, direction0);
        if (node0 != null) {
            node0.onConnectionRemoved(serverLevel, pos, direction0.getOpposite());
        }
        NetworkNode<T> node1 = this.findNextNode(null, serverLevel, pos, direction1);
        if (node1 != null) {
            node1.onConnectionRemoved(serverLevel, pos, direction1.getOpposite());
        }
    }

    public void addInteractor(ServerLevel serverLevel, BlockPos interactorPos) {
        NodeNetworkSavedData networks = NodeNetworkSavedData.getNetworks(serverLevel);
        networks.getInteractors().computeIfAbsent(this, k -> new HashSet<>()).add(interactorPos);
        networks.setDirty();
        if (this.isSynced()) {
            PacketDistributor.sendToAllPlayers(new AddInteractorPayload(this, interactorPos));
        }

    }

    public void removeInteractor(ServerLevel serverLevel, BlockPos interactorPos) {
        NodeNetworkSavedData networks = NodeNetworkSavedData.getNetworks(serverLevel);
        networks.getInteractors().computeIfAbsent(this, k -> new HashSet<>()).remove(interactorPos);
        networks.setDirty();
        if (this.isSynced()) {
            PacketDistributor.sendToAllPlayers(new RemoveInteractorPayload(this, interactorPos));
        }

    }

    public void addNodeAndUpdate(ServerLevel level, BlockPos pos, Direction[] connections, boolean dead, @Nullable BlockPos interactorPos, @Nullable Direction interactorConnection) {
        NetworkNode<T> node = this.createNode(pos);
        node.setDead(dead);
        node.setInteractorConnection(interactorConnection);
        if (interactorPos != null) {
            this.addInteractor(level, interactorPos);
        }
        this.addNode(level, pos, node);

        if (!dead) {
            for (Direction connection : connections) {
                if (connection != null) {
                    NetworkNode<T> nextNode = this.findNextNode(node, level, pos, connection);
                    if (nextNode != null && !nextNode.isDead()) {
                        node.addNextNodeSynced(connection, nextNode);
                        nextNode.onNextNodeAdded(node, connection.getOpposite());
                    }
                }
            }
        }
    }

    public void addNode(ServerLevel level, BlockPos pos, NetworkNode<T> node) {
        NodeNetworkSavedData networks = NodeNetworkSavedData.getNetworks(level);
        getServerNodes(level).put(pos, node);
        networks.setDirty();
        if (this.isSynced()) {
            PacketDistributor.sendToAllPlayers(new AddNetworkNodePayload<>(this, pos, node));
        }

    }

    public void removeNodeAndUpdate(ServerLevel serverLevel, BlockPos pos) {
        NetworkNode<T> node = this.removeNode(serverLevel, pos);

        for (Map.Entry<Direction, NetworkNode<T>> nextNode : node.getNext().entrySet()) {
            NetworkNode<T> node1 = nextNode.getValue();
            Direction direction = nextNode.getKey();
            if (node1 != null) {
                node1.removeNextNodeSynced(direction.getOpposite());
            }
        }

        if (node.getInteractorConnection() != null) {
            BlockPos relative = pos.relative(node.getInteractorConnection());
            this.removeInteractor(serverLevel, relative);
            if (this.isSynced()) {
                PacketDistributor.sendToAllPlayers(new RemoveInteractorPayload(this, relative));
            }
        }
        this.setServerNodesChanged(serverLevel);

    }

    public @Nullable NetworkNode<T> removeNode(ServerLevel serverLevel, BlockPos pos) {
        NodeNetworkSavedData networks = NodeNetworkSavedData.getNetworks(serverLevel);
        NetworkNode<T> removedNode = (NetworkNode<T>) getServerNodes(serverLevel).remove(pos);
        networks.setDirty();
        if (this.isSynced()) {
            PacketDistributor.sendToAllPlayers(new RemoveNetworkNodePayload<>(this, pos));
        }
        return removedNode;
    }

    public @Nullable NetworkNode<T> getNode(ServerLevel serverLevel, BlockPos pos) {
        return (NetworkNode<T>) getServerNodes(serverLevel).get(pos);
    }

    public boolean hasNodeAt(ServerLevel serverLevel, BlockPos pos) {
        return getServerNodes(serverLevel).containsKey(pos);
    }

    public boolean checkForInteractorAt(ServerLevel serverLevel, BlockPos nodePos, Direction direction) {
        return this.interactorCheckFunction.test(serverLevel, nodePos, direction);
    }

    public boolean hasInteractorAt(ServerLevel serverLevel, BlockPos interactorPos) {
        NodeNetworkSavedData networks = NodeNetworkSavedData.getNetworks(serverLevel);
        return networks.getInteractors().getOrDefault(this, Collections.emptySet()).contains(interactorPos);
    }

    public Map<BlockPos, NetworkNode<?>> getServerNodes(ServerLevel level) {
        NodeNetworkSavedData networkSavedData = NodeNetworkSavedData.getNetworks(level);
        Map<TransportNetwork<?>, Map<BlockPos, NetworkNode<?>>> networks = networkSavedData.getNetworkNodes();
        Map<BlockPos, NetworkNode<?>> map;
        if (!networks.containsKey(this)) {
            map = new HashMap<>();
            networks.put(this, map);
            networkSavedData.setDirty();
        } else {
            map = networks.get(this);
        }
        return map;
    }

    public void setServerNodesChanged(ServerLevel serverLevel) {
        NodeNetworkSavedData.getNetworks(serverLevel).setDirty();
    }

    public @Nullable NetworkNode<T> findNextNode(@Nullable NetworkNode<T> selfNode, ServerLevel serverLevel, BlockPos pos, Direction direction) {
        return this.findNextNode(selfNode, serverLevel, pos, direction, Set.of());
    }

    public @Nullable NetworkNode<T> findNextNode(@Nullable NetworkNode<T> selfNode, ServerLevel serverLevel, BlockPos pos, Direction direction, Set<BlockPos> ignoredNodes) {
        Map<BlockPos, NetworkNode<?>> nodes = this.getServerNodes(serverLevel);
        Set<BlockPos> alignedPositions = new HashSet<>();

        for (Map.Entry<BlockPos, NetworkNode<?>> node1 : nodes.entrySet()) {
            if (node1.getValue() != selfNode) {
                BlockPos pos1 = node1.getKey();
                if (!ignoredNodes.contains(pos1)) {
                    if (areNodesAligned(pos, pos1, direction)) {
                        alignedPositions.add(pos1);
                    }
                }
            }
        }

        if (!alignedPositions.isEmpty()) {
            BlockPos nearestPos;
            if (alignedPositions.size() == 1) {
                nearestPos = alignedPositions.stream().findFirst().get();
            } else {
                nearestPos = sortByDirectionalDistance(pos, alignedPositions, direction).findFirst().get();
            }

            if (this.maxConnectionDistance >= 0) {
                int dx = Math.abs(pos.getX() - nearestPos.getX());
                int dy = Math.abs(pos.getY() - nearestPos.getY());
                int dz = Math.abs(pos.getZ() - nearestPos.getZ());

                if (dx > maxConnectionDistance || dy > maxConnectionDistance || dz > maxConnectionDistance) {
                    return null;
                }
            }

            return this.getNode(serverLevel, nearestPos);
        }

        return null;
    }

    /**
     * @param value The value to be transported
     * @param directions the directions the value should be transported to (only relevant for initial node). If the array is empty, the value will be transported in all directions
     * @return the remaining value that was not transported anywhere, returns {@link TransportingHandler#defaultValue()} if everything was distributed
     */
    public T transport(ServerLevel serverLevel, BlockPos pos, T value, Direction... directions) {
        if (this.getTransportingHandler().validTransportValue(value) && this.hasInteractorAt(serverLevel, pos)) {
            Direction[] directions1;
            if (directions.length == 0) {
                directions1 = Direction.values();
            } else {
                directions1 = directions;
            }
            List<NetworkNode<T>> nodes = new ArrayList<>();
            for (Direction direction : directions1) {
                BlockPos relative = pos.relative(direction);
                if (this.hasNodeAt(serverLevel, relative)) {
                    nodes.add(this.getNode(serverLevel, relative));
                }
            }

            List<T> split = this.transportingHandler.split(value, nodes.size());

            for (int i = 0; i < nodes.size(); i++) {
                nodes.get(i).getTransporting().setValue(split.get(i));
            }

            // TODO: Return remainder
            return getTransportingHandler().defaultValue();
        }
        return value;
    }

    private static boolean areNodesAligned(BlockPos pos0, BlockPos pos1, Direction direction) {
        int deltaX = Integer.signum(pos1.getX() - pos0.getX());
        int deltaY = Integer.signum(pos1.getY() - pos0.getY());
        int deltaZ = Integer.signum(pos1.getZ() - pos0.getZ());

        return deltaX == direction.getStepX() && deltaY == direction.getStepY() && deltaZ == direction.getStepZ();
    }

    private static Stream<BlockPos> sortByDirectionalDistance(BlockPos mainPos, Set<BlockPos> positions, Direction direction) {
        return positions.stream()
                .sorted(Comparator.comparingInt(pos -> getDirectionalDistance(mainPos, pos, direction)));
    }

    private static int getDirectionalDistance(BlockPos origin, BlockPos target, Direction direction) {
        int diff;
        switch (direction.getAxis()) {
            case X -> diff = target.getX() - origin.getX();
            case Y -> diff = target.getY() - origin.getY();
            case Z -> diff = target.getZ() - origin.getZ();
            default -> throw new IllegalArgumentException("Invalid axis");
        }

        if (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
            diff = -diff;
        }

        return diff;
    }

    public int getMaxConnectionDistance() {
        return maxConnectionDistance;
    }

    public boolean isSynced() {
        return streamCodec != null;
    }

    public TransportingHandler<T> getTransportingHandler() {
        return transportingHandler;
    }

    public Codec<T> codec() {
        return transportingCodec;
    }

    public StreamCodec<ByteBuf, T> streamCodec() {
        return streamCodec;
    }

    public static <T> Builder<T> builder(BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> factory, Codec<T> codec, TransportingHandler<T> transportingHandler) {
        return new Builder<>(factory, codec, transportingHandler);
    }

    public static final class Builder<T> {
        private final BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> nodeFactory;
        private final Codec<T> transportingCodec;
        private final TransportingHandler<T> transportingHandler;
        private BiFunction<ServerLevel, NetworkNode<T>, Float> lossPerBlockFunction = (l, n) -> 0F;
        private BiFunction<ServerLevel, NetworkNode<T>, Float> transferSpeedFunction = (l, n) -> 1F;
        private TriPredicate<Level, BlockPos, Direction> interactorCheckFunction = (l, p, d) -> false;
        private int maxConnectionDistance = -1;
        private StreamCodec<ByteBuf, T> streamCodec = null;

        private Builder(BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> factory, Codec<T> transportingCodec, TransportingHandler<T> transportingHandler) {
            this.nodeFactory = factory;
            this.transportingCodec = transportingCodec;
            this.transportingHandler = transportingHandler;
        }

        public Builder<T> lossPerBlock(BiFunction<ServerLevel, NetworkNode<T>, Float> lossPerBlockFunction) {
            this.lossPerBlockFunction = lossPerBlockFunction;
            return this;
        }

        public Builder<T> transferSpeed(BiFunction<ServerLevel, NetworkNode<T>, Float> transferSpeedFunction) {
            this.transferSpeedFunction = transferSpeedFunction;
            return this;
        }

        public Builder<T> interactorCheck(TriPredicate<Level, BlockPos, Direction> interactorCheckFunction) {
            this.interactorCheckFunction = interactorCheckFunction;
            return this;
        }

        public Builder<T> maxConnectionDistance(int maxConnectionDistance) {
            this.maxConnectionDistance = maxConnectionDistance;
            return this;
        }

        public Builder<T> synced(StreamCodec<ByteBuf, T> streamCodec) {
            this.streamCodec = streamCodec;
            return this;
        }

        public TransportNetwork<T> build() {
            return new TransportNetwork<>(this);
        }
    }
}
