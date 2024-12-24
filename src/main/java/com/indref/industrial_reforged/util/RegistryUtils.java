package com.indref.industrial_reforged.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RegistryUtils {
    public static <T> Codec<T> registryCodec(Registry<T> registry) {
        return ResourceLocation.CODEC.xmap(registry::get, registry::getKey);
    }

    public static <T> StreamCodec<ByteBuf, T> registryStreamCodec(Registry<T> registry) {
        return ResourceLocation.STREAM_CODEC.map(registry::get, registry::getKey);
    }

    /**
     * Returns the resource key for a value on that registry.
     */
    public static <T> ResourceKey<T> resourceKey(Registry<T> registry, T value) {
        return registry.getResourceKey(value).orElseThrow();
    }

    /**
     * Returns the holder reference for a value on that registry.
     */
    public static <T> Holder.Reference<T> holder(Registry<T> registry, T value) {
        return registry.getHolderOrThrow(resourceKey(registry, value));
    }
}
