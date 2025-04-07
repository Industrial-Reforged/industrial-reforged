package com.indref.industrial_reforged.transportation.energy;

import com.indref.industrial_reforged.content.blocks.pipes.CableBlock;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class NetworkNode {
    private final BlockPos pos;
    private final Map<Direction, NetworkNode> next;

    public NetworkNode(BlockPos pos) {
        this.pos = pos;
        this.next = new HashMap<>();
    }

    public void invalidate(Level level, NetworkNode removedNode, Direction removedDirection) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof CableBlock) {
            boolean connected = blockState.getValue(CableBlock.CONNECTION[removedDirection.get3DDataValue()]);
            BlockPos relative = pos.relative(removedDirection);
            if (connected) {
                if (NetworkManager.hasNode(relative)/* || CableBlock.shouldHaveNode(level, relative)*/) {
                    next.put(removedDirection, NetworkManager.getNode(relative));
                } else {
                    NetworkNode nextNode = NetworkManager.findNextNode(this, pos, removedDirection);
                    if (nextNode != null) {
                        next.put(removedDirection, nextNode);
                    } else {
                        next.remove(removedDirection);
                    }
                }
            } else {
                NetworkNode nextNode = NetworkManager.findNextNode(this, pos, removedDirection);
                if (nextNode != null) {
                    next.put(removedDirection, nextNode);
                } else {
                    next.remove(removedDirection);
                }
            }
        }
    }

    public BlockPos getPos() {
        return pos;
    }

    public Map<Direction, NetworkNode> getNext() {
        return next;
    }
}
