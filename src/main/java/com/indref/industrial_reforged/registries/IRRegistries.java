package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class IRRegistries {
    private static final ResourceKey<Registry<IMultiblock>> MULTIBLOCK_KEY = ResourceKey.createRegistryKey(new ResourceLocation("indref:multiblock"));
    public static final Registry<IMultiblock> MULTIBLOCK = new RegistryBuilder<>(MULTIBLOCK_KEY).create();
}
