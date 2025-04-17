package com.indref.industrial_reforged.api.transportation;

import com.indref.industrial_reforged.data.saved.NodeNetworkSavedData;
import com.indref.industrial_reforged.networking.transportation.AddNetworkNodePayload;
import com.indref.industrial_reforged.networking.transportation.AddNextNodePayload;
import com.indref.industrial_reforged.networking.transportation.RemoveNetworkNodePayload;
import com.indref.industrial_reforged.networking.transportation.RemoveNextNodePayload;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
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
    private final Supplier<T> defaultValueSupplier;
    private final Function<BlockState, Float> lossPerBlockFunction;
    private final Function<BlockState, Float> transferSpeedFunction;
    private final int maxConnectionDistance;
    private final StreamCodec<ByteBuf, T> streamCodec;

    private TransportNetwork(Builder<T> builder) {
        this.nodeFactory = builder.nodeFactory;
        this.transportingCodec = builder.transportingCodec;
        this.defaultValueSupplier = builder.defaultValueSupplier;
        this.lossPerBlockFunction = builder.lossPerBlockFunction;
        this.transferSpeedFunction = builder.transferSpeedFunction;
        this.maxConnectionDistance = builder.maxConnectionDistance;
        this.streamCodec = builder.streamCodec;
    }

    public NetworkNode<T> createNode(BlockPos pos) {
        return this.nodeFactory.apply(this, pos);
    }

    public void addNodeAndUpdate(ServerLevel level, BlockPos pos, Direction[] connections, boolean dead) {
        NetworkNode<T> node = this.createNode(pos);
        node.setDead(dead);
        this.addNode(level, pos, node);
        Map<Direction, NetworkNode<T>> next = node.getNext();
        for (Direction direction : connections) {
            if (direction != null) {
                BlockPos relative = pos.relative(direction);
                if (this.hasNodeAt(level, relative)) {
                    NetworkNode<T> nextNode = this.getNode(level, relative);
                    if (!nextNode.isDead() && !node.isDead()) {
                        next.put(direction, nextNode);
                        nextNode.getNext().put(direction.getOpposite(), node);
                        this.setServerNodesChanged(level);
                        if (this.isSynced()) {
                            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this, pos, direction, relative));
                            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this, relative, direction.getOpposite(), pos));
                        }
                    }
                } else {
                    NetworkNode<T> nextNode = this.findNextNode(node, level, pos, direction);
                    if (nextNode != null && !nextNode.isDead() && !node.isDead()) {
                        next.put(direction, nextNode);
                        nextNode.getNext().put(direction.getOpposite(), node);
                        this.setServerNodesChanged(level);
                        if (this.isSynced()) {
                            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this, pos, direction, nextNode.getPos()));
                            PacketDistributor.sendToAllPlayers(new AddNextNodePayload(this, nextNode.getPos(), direction.getOpposite(), pos));
                        }
                    }
                }
            }
        }
    }

    public void addNode(ServerLevel level, BlockPos pos, NetworkNode<T> node) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            NodeNetworkSavedData networks = NodeNetworkSavedData.getNetworks(serverLevel);
            getServerNodes(serverLevel).put(pos, node);
            networks.setDirty();
            if (this.isSynced()) {
                PacketDistributor.sendToAllPlayers(new AddNetworkNodePayload<>(this, pos, node));
            }
        }
    }

    public void removeNodeAndUpdate(ServerLevel serverLevel, BlockPos pos) {
        NetworkNode<T> node = this.removeNode(serverLevel, pos);

        for (Map.Entry<Direction, ? extends NetworkNode<?>> nextNode : node.getNext().entrySet()) {
            NetworkNode<T> node1 = (NetworkNode<T>) nextNode.getValue();
            Direction direction = nextNode.getKey();
            if (node1 != null) {
                node1.setChanged(serverLevel, node, direction);
                node1.getNext().remove(direction.getOpposite());

                if (this.isSynced()) {
                    PacketDistributor.sendToAllPlayers(new RemoveNextNodePayload(this, node1.getPos(), direction.getOpposite()));
                }
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
        Map<BlockPos, NetworkNode<?>> nodes = this.getServerNodes(serverLevel);
        Set<BlockPos> alignedPositions = new HashSet<>();

        for (Map.Entry<BlockPos, NetworkNode<?>> node1 : nodes.entrySet()) {
            if (node1.getValue() != selfNode) {
                BlockPos pos1 = node1.getKey();
                if (areNodesAligned(pos, pos1, direction)) {
                    alignedPositions.add(pos1);
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
            return this.getNode(serverLevel, nearestPos);
        }

        return null;
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

    public boolean isSynced() {
        return streamCodec != null;
    }

    public Supplier<T> defaultValueSupplier() {
        return defaultValueSupplier;
    }

    public Codec<T> codec() {
        return transportingCodec;
    }

    public StreamCodec<ByteBuf, T> streamCodec() {
        return streamCodec;
    }

    public static <T> Builder<T> builder(BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> factory, Codec<T> codec, Supplier<T> defaultValueSupplier) {
        return new Builder<>(factory, codec, defaultValueSupplier);
    }

    public static final class Builder<T> {
        private final BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> nodeFactory;
        private final Codec<T> transportingCodec;
        private final Supplier<T> defaultValueSupplier;
        private Function<BlockState, Float> lossPerBlockFunction = state -> 0F;
        private Function<BlockState, Float> transferSpeedFunction = state -> 1F;
        private int maxConnectionDistance = -1;
        private StreamCodec<ByteBuf, T> streamCodec = null;

        private Builder(BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> factory, Codec<T> transportingCodec, Supplier<T> defaultValueSupplier) {
            this.nodeFactory = factory;
            this.transportingCodec = transportingCodec;
            this.defaultValueSupplier = defaultValueSupplier;
        }

        public Builder<T> lossPerBlock(Function<BlockState, Float> lossPerBlockFunction) {
            this.lossPerBlockFunction = lossPerBlockFunction;
            return this;
        }

        public Builder<T> transferSpeed(Function<BlockState, Float> transferSpeedFunction) {
            this.transferSpeedFunction = transferSpeedFunction;
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
