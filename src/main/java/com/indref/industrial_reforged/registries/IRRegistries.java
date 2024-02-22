package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class IRRegistries {
    private static final ResourceKey<Registry<Multiblock>> MULTIBLOCK_KEY = ResourceKey.createRegistryKey(new ResourceLocation("indref:multiblock"));
    public static final Registry<Multiblock> MULTIBLOCK = new RegistryBuilder<>(MULTIBLOCK_KEY).create();
}
