package com.indref.industrial_reforged.tags;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class IRTags {
    public static class Blocks {
        public static final TagKey<Block> RUBBER_LOG = bind("minecraft", "rubber_logs");

        private static TagKey<Block> bind(String path) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, path));
        }

        private static TagKey<Block> bind(String namespace, String path) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, path));
        }
    }

    public static class Items {
        public static final TagKey<Item> RUBBER_LOGS = bind("minecraft", "rubber_logs");
        public static final TagKey<Item> TOOL = bind("tool");

        private static TagKey<Item> bind(String path) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, path));
        }

        private static TagKey<Item> bind(String namespace, String path) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
        }
    }
}
