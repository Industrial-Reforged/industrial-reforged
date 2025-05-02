package com.indref.industrial_reforged.api.transportation.cache;

import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.Objects;
import java.util.Set;

// TODO: Encode path as set of blockpos
public class NetworkRoute<T> {
    private final BlockPos originPos;
    private final Set<NetworkNode<T>> path;
    private BlockPos interactorDest;
    private Direction interactorDirection;
    private int physicalDistance;
    private boolean valid;

    public NetworkRoute(BlockPos originPos, Set<NetworkNode<T>> path) {
        this.originPos = originPos;
        this.path = path;
    }

    public static <T> Codec<NetworkRoute<T>> codec(TransportNetwork<T> network) {
        return RecordCodecBuilder.create(inst -> inst.group(
                BlockPos.CODEC.fieldOf("origin_pos").forGetter(NetworkRoute::getOriginPos),
                BlockPos.CODEC.fieldOf("interactor_dest").forGetter(NetworkRoute::getInteractorDest),
                Direction.CODEC.fieldOf("interator_dir").forGetter(NetworkRoute::getInteractorDirection),
                Codec.INT.fieldOf("physical_distance").forGetter(NetworkRoute::getPhysicalDistance),
                CodecUtils.set(NetworkNode.codec(network)).fieldOf("path").forGetter(NetworkRoute::getPath)
        ).apply(inst, NetworkRoute::codecNew));
    }

    public void setPhysicalDistance(int physicalDistance) {
        this.physicalDistance = physicalDistance;
    }

    public void setInteractorDest(BlockPos interactorDest) {
        this.interactorDest = interactorDest;
    }

    public void setInteractorDirection(Direction interactorDirection) {
        this.interactorDirection = interactorDirection;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Direction getInteractorDirection() {
        return interactorDirection;
    }

    public int getPhysicalDistance() {
        return physicalDistance;
    }

    public BlockPos getOriginPos() {
        return originPos;
    }

    public BlockPos getInteractorDest() {
        return interactorDest;
    }

    public Set<NetworkNode<T>> getPath() {
        return path;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (NetworkRoute<?>) obj;
        return Objects.equals(this.originPos, that.originPos) &&
                Objects.equals(this.interactorDest, that.interactorDest) &&
                Objects.equals(this.path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originPos, interactorDest, path);
    }

    @Override
    public String toString() {
        return "NetworkRoute[" +
                "originPos=" + originPos + ", " +
                "interactorDestinations=" + interactorDest + ", " +
                "path=" + path + ']';
    }

    private static <T> NetworkRoute<T> codecNew(BlockPos originPos, BlockPos interactorDest, Direction interactorDirection, int physicalDistance, Set<NetworkNode<T>> path) {
        NetworkRoute<T> route = new NetworkRoute<>(originPos, path);
        route.interactorDest = interactorDest;
        route.physicalDistance = physicalDistance;
        route.interactorDirection = interactorDirection;
        return route;
    }

}
