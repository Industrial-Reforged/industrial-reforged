package com.indref.industrial_reforged.transportation.energy;

import com.indref.industrial_reforged.content.blocks.pipes.CableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkManager {
    public static final Map<BlockPos, NetworkNode> NODES = new ConcurrentHashMap<>();

    public static boolean hasNode(BlockPos pos) {
        return NODES.containsKey(pos);
    }

    public static void addNode(BlockPos pos) {
        NODES.put(pos, new NetworkNode(pos));
    }

    public static void addNode(Level level, BlockPos pos, Direction[] connections) {
        NetworkNode node = new NetworkNode(pos);
        Map<Direction, NetworkNode> next = node.getNext();
        for (Direction direction : connections) {
            if (direction != null) {
                BlockPos relative = pos.relative(direction);
                if (NetworkManager.hasNode(relative) || CableBlock.shouldHaveNode(level, relative)) {
                    next.put(direction, getNode(relative));
                } else {
                    NetworkNode nextNode = findNextNode(node, pos, direction);
                    if (nextNode != null) {
                        next.put(direction, nextNode);
                    }
                }
            }
        }
        NODES.put(pos, node);
    }

    public static NetworkNode findNextNode(NetworkNode selfNode, BlockPos pos, Direction direction) {
        for (Map.Entry<BlockPos, NetworkNode> node1 : NODES.entrySet()) {
            if (node1.getValue() != selfNode) {
                BlockPos pos1 = node1.getKey();
                if (areNodesAligned(pos, pos1, direction)) {
                    return getNode(pos1);
                }
            }
        }
        return null;
    }

    public static boolean areNodesAligned(BlockPos pos0, BlockPos pos1, Direction direction) {
        // Only checks for axis, not the axis direction
        Direction.Axis axis = direction.getAxis();
        Direction.AxisDirection axisDirection = direction.getAxisDirection();
        return switch (axis) {
            case X -> pos0.getY() == pos1.getY() && pos0.getZ() == pos1.getZ()
                    && (axisDirection == Direction.AxisDirection.POSITIVE ? pos0.getX() > pos1.getX() : pos0.getX() < pos1.getX());
            case Y -> pos0.getX() == pos1.getX() && pos0.getZ() == pos1.getZ()
                    && (axisDirection == Direction.AxisDirection.POSITIVE ? pos0.getY() > pos1.getY() : pos0.getY() < pos1.getY());
            case Z -> pos0.getX() == pos1.getX() && pos0.getY() == pos1.getY()
                    && (axisDirection == Direction.AxisDirection.POSITIVE ? pos0.getZ() > pos1.getZ() : pos0.getZ() < pos1.getZ());
        };
    }

    public static void removeNode(Level level, BlockPos pos) {
        NetworkNode node = NODES.remove(pos);
        for (Map.Entry<Direction, NetworkNode> nextNode : node.getNext().entrySet()) {
            NetworkNode node1 = nextNode.getValue();
            Direction direction = nextNode.getKey();
            if (node1 != null) {
                node1.invalidate(level, node, direction.getOpposite());
            }
        }
    }

    public static NetworkNode getNode(BlockPos pos) {
        return NODES.get(pos);
    }
}
