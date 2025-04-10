package com.indref.industrial_reforged.api.transportation;

import com.indref.industrial_reforged.transportation.energy.NetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Function;

public class TransportNetwork<T> {
    private final BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> nodeFactory;
    private final Function<BlockState, Float> lossPerBlockFunction;
    private final Function<BlockState, Float> transferSpeedFunction;
    private final int maxConnectionDistance;

    private TransportNetwork(Builder<T> builder) {
        this.nodeFactory = builder.nodeFactory;
        this.lossPerBlockFunction = builder.lossPerBlockFunction;
        this.transferSpeedFunction = builder.transferSpeedFunction;
        this.maxConnectionDistance = builder.maxConnectionDistance;
    }

    public NetworkNode<T> createNode(BlockPos pos) {
        return this.nodeFactory.apply(this, pos);
    }

    public static <T> Builder<T> builder(BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> factory) {
        return new Builder<>(factory);
    }

    public static final class Builder<T> {
        private final BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> nodeFactory;
        private Function<BlockState, Float> lossPerBlockFunction = state -> 0F;
        private Function<BlockState, Float> transferSpeedFunction = state -> 1F;
        private int maxConnectionDistance = -1;

        private Builder(BiFunction<TransportNetwork<T>, BlockPos, NetworkNode<T>> factory) {
            this.nodeFactory = factory;
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

        public TransportNetwork<T> build() {
            return new TransportNetwork<>(this);
        }
    }
}
