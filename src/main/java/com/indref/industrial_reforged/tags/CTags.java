package com.indref.industrial_reforged.tags;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

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
        public static final TagKey<Item> IRON_PLATE = bind("plates/iron");
        public static final TagKey<Item> ALUMINUM_PLATE = bind("plates/aluminum");
        public static final TagKey<Item> COPPER_PLATE = bind("plates/copper");
        public static final TagKey<Item> STEEL_PLATE = bind("plates/steel");
        public static final TagKey<Item> TIN_PLATE = bind("plates/tin");
        public static final TagKey<Item> CARBON_PLATE = bind("plates/carbon");

        public static final TagKey<Item> WIRES = bind("wires");
        public static final TagKey<Item> TIN_WIRE = bind("wires/tin");
        public static final TagKey<Item> COPPER_WIRE = bind("wires/copper");
        public static final TagKey<Item> GOLD_WIRE = bind("wires/gold");
        public static final TagKey<Item> STEEL_WIRE = bind("wires/steel");

        public static final TagKey<Item> TIN_INGOT = bind("ingots/tin");
        public static final TagKey<Item> NICKEL_INGOT = bind("ingots/nickel");
        public static final TagKey<Item> LEAD_INGOT = bind("ingots/lead");
        public static final TagKey<Item> STEEL_INGOT = bind("ingots/steel");
        public static final TagKey<Item> ALUMINUM_INGOT = bind("ingots/aluminum");
        public static final TagKey<Item> CHROMIUM_INGOT = bind("ingots/chromium");
        public static final TagKey<Item> URANIUM_INGOT = bind("ingots/uranium");
        public static final TagKey<Item> TITANIUM_INGOT = bind("ingots/titanium");
        public static final TagKey<Item> IRIDIUM_INGOT = bind("ingots/iridium");

        public static final TagKey<Item> IRON_ROD = bind("rods/iron");
        public static final TagKey<Item> STEEL_ROD = bind("rods/steel");

        public static final TagKey<Item> STORAGE_BLOCKS_TIN = bind("storage_blocks/tin");
        public static final TagKey<Item> STORAGE_BLOCKS_NICKEL = bind("storage_blocks/nickel");
        public static final TagKey<Item> STORAGE_BLOCKS_STEEL = bind("storage_blocks/steel");
        public static final TagKey<Item> STORAGE_BLOCKS_LEAD = bind("storage_blocks/lead");
        public static final TagKey<Item> STORAGE_BLOCKS_URANIUM = bind("storage_blocks/uranium");
        public static final TagKey<Item> STORAGE_BLOCKS_IRIDIUM = bind("storage_blocks/iridium");
        public static final TagKey<Item> STORAGE_BLOCKS_TITANIUM = bind("storage_blocks/titanium");
        public static final TagKey<Item> STORAGE_BLOCKS_ALUMINUM = bind("storage_blocks/aluminum");

        public static final TagKey<Item> DUSTS_COPPER = bind("dusts/copper");
        public static final TagKey<Item> DUSTS_STEEL = bind("dusts/steel");

        public static final TagKey<Item> RAW_MATERIALS_BAUXITE = bind("raw_materials/bauxite");
        public static final TagKey<Item> RAW_MATERIALS_CHROMIUM = bind("raw_materials/chromium");
        public static final TagKey<Item> RAW_MATERIALS_IRIDIUM = bind("raw_materials/iridium");
        public static final TagKey<Item> RAW_MATERIALS_LEAD = bind("raw_materials/lead");
        public static final TagKey<Item> RAW_MATERIALS_NICKEL = bind("raw_materials/nickel");
        public static final TagKey<Item> RAW_MATERIALS_TIN = bind("raw_materials/tin");
        public static final TagKey<Item> RAW_MATERIALS_URANIUM = bind("raw_materials/uranium");

        public static final TagKey<Item> ORES_BAUXITE = bind("ores/bauxite");
        public static final TagKey<Item> ORES_CHROMIUM = bind("ores/chromium");
        public static final TagKey<Item> ORES_IRIDIUM = bind("ores/iridium");
        public static final TagKey<Item> ORES_LEAD = bind("ores/lead");
        public static final TagKey<Item> ORES_NICKEL = bind("ores/nickel");
        public static final TagKey<Item> ORES_TIN = bind("ores/tin");
        public static final TagKey<Item> ORES_URANIUM = bind("ores/uranium");

        public static final TagKey<Item> STORAGE_BLOCKS_RAW_TIN = bind("storage_blocks/raw_tin");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_NICKEL = bind("storage_blocks/raw_nickel");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_LEAD = bind("storage_blocks/raw_lead");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_URANIUM = bind("storage_blocks/raw_uranium");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_IRIDIUM = bind("storage_blocks/raw_iridium");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_BAUXITE = bind("storage_blocks/raw_titanium");

        public static final TagKey<Item> RUBBER = bind("rubber");

        private static TagKey<Item> bind(String path) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
        }
    }

    public static class Fluids {
        public static final TagKey<Fluid> OIL = bind("oil");

        private static TagKey<Fluid> bind(String path) {
            return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath("c", path));
        }
    }
}
