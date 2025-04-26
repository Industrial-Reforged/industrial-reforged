package com.indref.industrial_reforged.api.transportation;

import com.indref.industrial_reforged.IRRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Transporting<T> {
    private final TransportNetwork<T> network;
    private @Nullable T value;

    public Transporting(TransportNetwork<T> network) {
        this.network = network;
    }

    public void setValue(@Nullable T value) {
        this.value = value;
    }

    public @Nullable T getValue() {
        return value != null ? value : network.getTransportingHandler().defaultValue();
    }

    public T removeValue() {
        T valueCopy = this.value;
        this.value = null;
        return valueCopy;
    }

    public TransportNetwork<T> getNetwork() {
        return network;
    }

    @Override
    public String toString() {
        return "Transporting{value=%s}".formatted(getValue());
    }

    private static <T> Transporting<T> fromValue(TransportNetwork<?> network, T value) {
        Transporting<T> transporting = new Transporting<>((TransportNetwork<T>) network);
        transporting.setValue(value);
        return transporting;
    }

    public static <T> Codec<Transporting<T>> codec(Codec<T> valueCodec) {
        return RecordCodecBuilder.create(inst -> inst.group(
                CodecUtils.registryCodec(IRRegistries.NETWORK).fieldOf("network").forGetter(Transporting::getNetwork),
                valueCodec.fieldOf("value").forGetter(Transporting::getValue)
        ).apply(inst, Transporting::fromValue));
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, Transporting<T>> streamCodec(StreamCodec<ByteBuf, T> codec) {
        return StreamCodec.composite(
                CodecUtils.registryStreamCodec(IRRegistries.NETWORK),
                Transporting::getNetwork,
                codec,
                Transporting::getValue,
                Transporting::fromValue
        );
    }

}
