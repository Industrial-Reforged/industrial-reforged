package com.indref.industrial_reforged.transportation.energy;

import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.api.transportation.Transporting;
import com.indref.industrial_reforged.content.blocks.pipes.CableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class NetworkNode<T> {
    private final BlockPos pos;
    private final Map<Direction, NetworkNode<T>> next;
    private final Transporting<T> transporting;

    public NetworkNode(TransportNetwork<T> network, BlockPos pos) {
        this.pos = pos;
        this.next = new HashMap<>();
        this.transporting = new Transporting<>();
    }

    public void setChanged(Level level, NetworkNode<T> originNode, Direction changedDirection) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof CableBlock) {
            boolean connected = blockState.getValue(CableBlock.CONNECTION[changedDirection.get3DDataValue()]);
            BlockPos relative = pos.relative(changedDirection);
            if (connected) {
                if (NetworkManager.hasNode(relative)) {
                    next.put(changedDirection, NetworkManager.getNodeByType(relative));
                } else {
                    NetworkNode<T> nextNode = NetworkManager.findNextNode(this, pos, changedDirection);
                    if (nextNode != null) {
                        next.put(changedDirection, nextNode);
                    } else {
                        next.remove(changedDirection);
                    }
                }
            } else {
                NetworkNode<T> nextNode = NetworkManager.findNextNode(this, pos, changedDirection);
                if (nextNode != null) {
                    next.put(changedDirection, nextNode);
                } else {
                    next.remove(changedDirection);
                }
            }
        }
    }

    public BlockPos getPos() {
        return pos;
    }

    public Map<Direction, NetworkNode<T>> getNext() {
        return next;
    }
}
