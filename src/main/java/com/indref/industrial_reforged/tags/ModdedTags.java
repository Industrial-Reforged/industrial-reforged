package com.indref.industrial_reforged.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModdedTags {
    public static final class Blocks {
        public static final TagKey<Block> SUPPORTS_FACADE = bind("cable_facades", "supports_facade");

        public static TagKey<Block> bind(String modid, String path) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modid, path));
        }
    }
}
