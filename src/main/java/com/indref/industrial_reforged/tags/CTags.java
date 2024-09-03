package com.indref.industrial_reforged.tags;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class CTags {
    public static class Blocks {
        private static TagKey<Block> bind(String path) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, path));
        }

        private static TagKey<Block> bind(String namespace, String path) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, path));
        }
    }

    public static class Items {
        public static final TagKey<Item> PLATES = bind("plates");
        public static final TagKey<Item> RUBBER_SHEET = bind("plates/rubber");
        public static final TagKey<Item> IRON_PLATE = bind("plates/iron");
        public static final TagKey<Item> COPPER_PLATE = bind("plates/copper");
        public static final TagKey<Item> STEEL_PLATE = bind("plates/steel");
        public static final TagKey<Item> TIN_PLATE = bind("plates/tin");

        public static final TagKey<Item> WIRES = bind("wires");
        public static final TagKey<Item> TIN_WIRE = bind("wires/copper");
        public static final TagKey<Item> COPPER_WIRE = bind("wires/copper");
        public static final TagKey<Item> GOLD_WIRE = bind("wires/copper");
        public static final TagKey<Item> STEEL_WIRE = bind("wires/copper");

        public static final TagKey<Item> INGOTS = bind("ingots");
        public static final TagKey<Item> TIN_INGOT = bind("ingots/tin");
        public static final TagKey<Item> NICKEL_INGOT = bind("ingots/nickel");
        public static final TagKey<Item> LEAD_INGOT = bind("ingots/lead");
        public static final TagKey<Item> STEEL_INGOT = bind("ingots/steel");
        public static final TagKey<Item> ALUMINUM_INGOT = bind("ingots/aluminum");
        public static final TagKey<Item> CHROMIUM_INGOT = bind("ingots/chromium");
        public static final TagKey<Item> URANIUM_INGOT = bind("ingots/uranium");
        public static final TagKey<Item> TITANIUM_INGOT = bind("ingots/titanium");
        public static final TagKey<Item> IRIDIUM_INGOT = bind("ingots/titanium");


        private static TagKey<Item> bind(String path) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
        }
    }
}
