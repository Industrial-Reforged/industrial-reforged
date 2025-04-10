package com.indref.industrial_reforged.transportation.energy;

import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkManager {
    public static final Map<BlockPos, NetworkNode<?>> NODES = new ConcurrentHashMap<>();

    public static boolean hasNode(BlockPos pos) {
        return NODES.containsKey(pos);
    }

    public static <T> void addNode(TransportNetwork<T> network, Level level, BlockPos pos, Direction[] connections) {
        NetworkNode<T> node = network.createNode(pos);
        Map<Direction, NetworkNode<T>> next = node.getNext();
        for (Direction direction : connections) {
            if (direction != null) {
                BlockPos relative = pos.relative(direction);
                if (NetworkManager.hasNode(relative)) {
                    next.put(direction, getNodeByType(relative));
                } else {
                    NetworkNode<T> nextNode = findNextNode(node, pos, direction);
                    if (nextNode != null) {
                        next.put(direction, nextNode);
                    }
                }
            }
        }
        NODES.put(pos, node);
    }

    public static <T> NetworkNode<T> findNextNode(NetworkNode<T> selfNode, BlockPos pos, Direction direction) {
        for (Map.Entry<BlockPos, NetworkNode<?>> node1 : NODES.entrySet()) {
            if (node1.getValue() != selfNode) {
                BlockPos pos1 = node1.getKey();
                if (areNodesAligned(pos, pos1, direction)) {
                    return getNodeByType(pos1);
                }
            }
        }
        return null;
    }

    public static boolean areNodesAligned(BlockPos pos0, BlockPos pos1, Direction direction) {
        int deltaX = Integer.signum(pos1.getX() - pos0.getX());
        int deltaY = Integer.signum(pos1.getY() - pos0.getY());
        int deltaZ = Integer.signum(pos1.getZ() - pos0.getZ());

        return deltaX == direction.getStepX() && deltaY == direction.getStepY() && deltaZ == direction.getStepZ();
    }

    public static <T> void removeNode(Level level, BlockPos pos) {
        NetworkNode<T> node = (NetworkNode<T>) NODES.remove(pos);
        for (Map.Entry<Direction, ? extends NetworkNode<?>> nextNode : node.getNext().entrySet()) {
            NetworkNode<T> node1 = (NetworkNode<T>) nextNode.getValue();
            Direction direction = nextNode.getKey();
            if (node1 != null) {
                node1.setChanged(level, node, direction);
            }
        }
    }

    public static NetworkNode<?> getNode(BlockPos pos) {
        return NODES.get(pos);
    }

    @SuppressWarnings("unchecked")
    public static <T> NetworkNode<T> getNodeByType(BlockPos pos) {
        return (NetworkNode<T>) NODES.get(pos);
    }

}
