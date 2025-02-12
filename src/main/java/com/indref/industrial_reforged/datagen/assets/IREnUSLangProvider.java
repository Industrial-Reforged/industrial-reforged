package com.indref.industrial_reforged.datagen.assets;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRFluids;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRTabs;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import com.indref.industrial_reforged.util.IRTranslations;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.indref.industrial_reforged.util.IRTranslations.Tooltip;
import static com.indref.industrial_reforged.util.IRTranslations.MultiblockFeedback;

public class IREnUSLangProvider extends LanguageProvider {
    public IREnUSLangProvider(PackOutput output) {
        super(output, IndustrialReforged.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlocks();
        addItems();
        addFluids();

        addMetals("bauxite", "Bauxite", true, false);

        addMetals("chromium", "Chromium");
        addMetals("iridium", "Iridium");
        addMetals("lead", "Lead");
        addMetals("nickel", "Nickel");
        addMetals("tin", "Tin");
        addMetals("uranium", "Uranium");

        addMetals("aluminum", "Aluminum", false, true);
        addMetals("steel", "Steel", false, true);
        addMetals("titanium", "Titanium", false, true);

        addCreativeTab(IRTabs.BLOCKS, "Industrial Reforged - Blocks");
        addCreativeTab(IRTabs.ITEMS, "Industrial Reforged - Items");

        addTooltip(Tooltip.ACTIVE, "Active");
        addTooltip(Tooltip.INACTIVE, "Inactive");
        addTooltip(Tooltip.ENERGY_STORED, "Stored");
        addTooltip(Tooltip.ENERGY_TIER, "Tier");
        addTooltip(Tooltip.FLUID_STORED, "Stored");
        addTooltip(Tooltip.FLUID_AMOUNT, "Amount");
        addTooltip(Tooltip.HEAT_STORED, "Heat: %d/%d");

        addTooltip(Tooltip.MELTING_PROGRESS, "Melting Progress");
        addTooltip(Tooltip.MELTING_NOT_POSSIBLE, "Melting not possible");

        addTooltip(Tooltip.LIQUID_AMOUNT, "%d/%d mb");

        addTooltip("drain", "A simple device for collecting fluids");

        addEnergyTier(EnergyTiers.NONE, "None");
        addEnergyTier(EnergyTiers.LOW, "Low");
        addEnergyTier(EnergyTiers.MEDIUM, "Medium");
        addEnergyTier(EnergyTiers.HIGH, "High");
        addEnergyTier(EnergyTiers.EXTREME, "Extreme");
        addEnergyTier(EnergyTiers.INSANE, "Insane");
        addEnergyTier(EnergyTiers.CREATIVE, "Creative");

        addTitle("firebox", "Firebox");
        addTitle("small_firebox", "Small Firebox");
        addTitle("crucible", "Crucible");
        addTitle("blueprint", "Blueprint");
        addTitle("crafting_station", "Crafting Station");
        addTooltip("basic_generator", "Basic Generator");
        addTooltip("centrifuge", "Centrifuge");

        addMultiblockFeedback(MultiblockFeedback.FAILED_TO_CONSTRUCT, "Missing or invalid block");
        addMultiblockFeedback(MultiblockFeedback.ACTUAL_BLOCK, "Block: %s");
        addMultiblockFeedback(MultiblockFeedback.EXPECTED_BLOCK, "Expected: %s");
        addMultiblockFeedback(MultiblockFeedback.BLOCK_POS, "Coordinates: %d, %d, %d");

        add("*.desc.melting_progress", "Melting Progress");
        add("*.desc.melting_not_possible", "Melting Not Possible");
    }

    private void addFluids() {
        addFluidType(IRFluids.OIL.getFluidType(), "Oil");
        addFluidType(IRFluids.BIO_MASS.getFluidType(), "Bio Mass");
        addFluidType(IRFluids.METHANE.getFluidType(), "Methane");

        addFluidType(IRFluids.MOLTEN_COPPER, "Molten Copper");
        addFluidType(IRFluids.MOLTEN_NICKEL, "Molten Nickel");
        addFluidType(IRFluids.MOLTEN_TIN, "Molten Tin");
        addFluidType(IRFluids.MOLTEN_ALUMINUM, "Molten Aluminum");
        addFluidType(IRFluids.MOLTEN_STEEL, "Molten Steel");
        addFluidType(IRFluids.MOLTEN_LEAD, "Molten Lead");
        addFluidType(IRFluids.MOLTEN_IRON, "Molten Iron");
        addFluidType(IRFluids.MOLTEN_GOLD, "Molten Gold");
    }

    private void addItems() {
        addItem(IRItems.FLUID_CELL, "Fluid Cell");
        addItem(IRItems.HAMMER, "Hammer");
        addItem(IRItems.WRENCH, "Wrench");
        addItem(IRItems.SCANNER, "Scanner");
        addItem(IRItems.THERMOMETER, "Thermometer");
        addItem(IRItems.NANO_SABER, "Nano Saber");
        addItem(IRItems.TOOLBOX, "Toolbox");
        addItem(IRItems.BLUEPRINT, "Blueprint");
        addItem(IRItems.FERTILIZER, "Fertilizer");
        addItem(IRItems.CLAY_MOLD_BLANK, "Clay Mold");
        addItem(IRItems.CLAY_MOLD_INGOT, "Clay Mold Ingot");
        addItem(IRItems.CLAY_MOLD_PLATE, "Clay Mold Plate");
        addItem(IRItems.CLAY_MOLD_WIRE, "Clay Mold Wire");
        addItem(IRItems.CLAY_MOLD_ROD, "Clay Mold Rod");
        addItem(IRItems.STICKY_RESIN, "Sticky Resin");
        addItem(IRItems.RUBBER, "Rubber");
        addItem(IRItems.RUBBER_SHEET, "Rubber Sheet");
        addItem(IRItems.PLANT_BALL, "Plant Ball");
        addItem(IRItems.TREE_TAP, "Tree Tap");
        addItem(IRItems.BASIC_BATTERY, "Basic Battery");
        addItem(IRItems.ADVANCED_BATTERY, "Advanced Battery");
        addItem(IRItems.ULTIMATE_BATTERY, "Ultimate Battery");
        addItem(IRItems.BASIC_CIRCUIT, "Basic Circuit");
        addItem(IRItems.ADVANCED_CIRCUIT, "Advanced Circuit");
        addItem(IRItems.ULTIMATE_CIRCUIT, "Ultimate Circuit");
        addItem(IRItems.CIRCUIT_BOARD, "Circuit Board");
        addItem(IRItems.ELECTRIC_MOTOR, "Electric Motor");
        addItem(IRItems.ANTENNA, "Antenna");
        addItem(IRItems.SANDY_BRICK, "Sandy Brick");
        addItem(IRItems.PLANT_MASS, "Plant Mass");
        addItem(IRItems.BIO_PLASTIC, "Bio Plastic");
        addItem(IRItems.TERRACOTTA_BRICK, "Terracotta Brick");

        addItem(IRItems.IRON_PLATE, "Iron Plate");
        addItem(IRItems.COPPER_PLATE, "Copper Plate");
        addItem(IRItems.STEEL_PLATE, "Steel Plate");
        addItem(IRItems.TIN_PLATE, "Tin Plate");
        addItem(IRItems.CARBON_PLATE, "Carbon Plate");
        addItem(IRItems.WOODEN_PLATE, "Wooden Plate");

        addItem(IRItems.IRON_ROD, "Iron Rod");
        addItem(IRItems.STEEL_ROD, "Steel Rod");

        addItem(IRItems.TIN_WIRE, "Tin Wire");
        addItem(IRItems.COPPER_WIRE, "Copper Wire");
        addItem(IRItems.GOLD_WIRE, "Gold Wire");
        addItem(IRItems.STEEL_WIRE, "Steel Wire");

        addItem(IRItems.COPPER_DUST, "Copper Dust");
        addItem(IRItems.COAL_DUST, "Coal Dust");
        addItem(IRItems.STEEL_DUST, "Steel Dust");

        addItem(IRFluids.OIL.getDeferredBucket(), "Oil Bucket");
        addItem(IRFluids.BIO_MASS.getDeferredBucket(), "Bio Mass Bucket");
        addItem(IRFluids.METHANE.getDeferredBucket(), "Methane Bucket");

        addItem(IRFluids.MOLTEN_COPPER.getDeferredBucket(), "Molten Copper Bucket");
        addItem(IRFluids.MOLTEN_NICKEL.getDeferredBucket(), "Molten Nickel Bucket");
        addItem(IRFluids.MOLTEN_TIN.getDeferredBucket(), "Molten Tin Bucket");
        addItem(IRFluids.MOLTEN_ALUMINUM.getDeferredBucket(), "Molten Aluminum Bucket");
        addItem(IRFluids.MOLTEN_STEEL.getDeferredBucket(), "Molten Steel Bucket");
        addItem(IRFluids.MOLTEN_LEAD.getDeferredBucket(), "Molten Lead Bucket");
        addItem(IRFluids.MOLTEN_IRON.getDeferredBucket(), "Molten Iron Bucket");
        addItem(IRFluids.MOLTEN_GOLD.getDeferredBucket(), "Molten Gold Bucket");

        addItem(IRItems.ELECTRIC_DRILL, "Electric Drill");
        addItem(IRItems.ADVANCED_DRILL, "Advanced Drill");
        addItem(IRItems.ELECTRIC_CHAINSAW, "Electric Chainsaw");
        addItem(IRItems.ADVANCED_CHAINSAW, "Advanced Chainsaw");
        addItem(IRItems.ELECTRIC_HOE, "Electric Hoe");
        addItem(IRItems.ELECTRIC_TREE_TAP, "Electric Tree Tap");
        addItem(IRItems.ROCK_CUTTER, "Rock Cutter");

        addItem(IRItems.HAZMAT_HELMET, "Hazmat Helmet");
        addItem(IRItems.HAZMAT_CHESTPLATE, "Hazmat Chestplate");
        addItem(IRItems.HAZMAT_LEGGINGS, "Hazmat Leggings");
        addItem(IRItems.HAZMAT_BOOTS, "Hazmat Boots");
    }

    private void addBlocks() {
        addBlock(IRBlocks.BASIC_GENERATOR, "Basic Generator");
        addBlock(IRBlocks.CENTRIFUGE, "Centrifuge");
        addBlock(IRBlocks.DRAIN, "Drain");
        addBlock(IRBlocks.CRAFTING_STATION, "Crafting Station");

        addBlock(IRBlocks.TIN_CABLE, "Tin Cable");
        addBlock(IRBlocks.COPPER_CABLE, "Copper Cable");
        addBlock(IRBlocks.GOLD_CABLE, "Gold Cable");
        addBlock(IRBlocks.STEEL_CABLE, "Steel Cable");

        addBlock(IRBlocks.BASIC_MACHINE_FRAME, "Basic Machine Frame");
//        addBlock(IRBlocks.MINING_PIPE, "Mining Pipe");

        addBlock(IRBlocks.REFRACTORY_BRICK, "Refractory Brick");
        addBlock(IRBlocks.REFRACTORY_STONE, "Refractory Stone");
        addBlock(IRBlocks.COIL, "Copper Coil");

        addBlock(IRBlocks.SMALL_FIREBOX_HATCH, "Small Firebox Hatch");
        addBlock(IRBlocks.IRON_FENCE, "Iron Fence");
        addBlock(IRBlocks.WOODEN_SCAFFOLDING, "Wooden Scaffolding");

        addBlock(IRBlocks.TERRACOTTA_BRICKS, "Terracotta Bricks");
        addBlock(IRBlocks.TERRACOTTA_BRICK_SLAB, "Terracotta Brick Slab");
        addBlock(IRBlocks.CERAMIC_CASTING_BASIN, "Ceramic Casting Basin");

        addBlock(IRBlocks.BLAST_FURNACE_BRICKS, "Blast Furnace Bricks");
        addBlock(IRBlocks.BLAST_FURNACE_HATCH, "Blast Furnace Hatch");
        addBlock(IRBlocks.BLAST_FURNACE_FAUCET, "Blast Furnace Faucet");
        addBlock(IRBlocks.BLAST_FURNACE_CASTING_BASIN, "Blast Furnace Casting Basin");

        addBlock(IRBlocks.RUBBER_TREE_LOG, "Rubber Tree Log");
        addBlock(IRBlocks.RUBBER_TREE_WOOD, "Rubber Tree Wood");
        addBlock(IRBlocks.STRIPPED_RUBBER_TREE_LOG, "Rubber Tree Stripped Log");
        addBlock(IRBlocks.STRIPPED_RUBBER_TREE_WOOD, "Rubber Tree Stripped Wood");
        addBlock(IRBlocks.RUBBER_TREE_LEAVES, "Rubber Tree Leaves");
        addBlock(IRBlocks.RUBBER_TREE_SAPLING, "Rubber Tree Sapling");
        addBlock(IRBlocks.RUBBER_TREE_PLANKS, "Rubber Tree Planks");
        addBlock(IRBlocks.RUBBER_TREE_DOOR, "Rubber Tree Door");
        addBlock(IRBlocks.RUBBER_TREE_TRAPDOOR, "Rubber Tree Trapdoor");
        addBlock(IRBlocks.RUBBER_TREE_FENCE, "Rubber Tree Fence");
        addBlock(IRBlocks.RUBBER_TREE_FENCE_GATE, "Rubber Tree Fence Gate");
        addBlock(IRBlocks.RUBBER_TREE_PRESSURE_PLATE, "Rubber Tree Pressure Plate");
        addBlock(IRBlocks.RUBBER_TREE_BUTTON, "Rubber Tree Button");
        addBlock(IRBlocks.RUBBER_TREE_SLAB, "Rubber Tree Slab");
        addBlock(IRBlocks.RUBBER_TREE_STAIRS, "Rubber Tree Stairs");
    }

    private void addMetals(String key, String val) {
        addMetals(key, val, true, true);
    }

    private void addMetals(String key, String val, boolean addOres, boolean addMetals) {
        if (addMetals) {
            add("block." + IndustrialReforged.MODID + "." + key + "_block", val + " Block");

            add("item." + IndustrialReforged.MODID + "." + key + "_ingot", val + " Ingot");
        }

        if (addOres) {
            add("block." + IndustrialReforged.MODID + "." + key + "_ore", val + " Ore");
            add("block." + IndustrialReforged.MODID + ".deepslate_" + key + "_ore", "Deepslate " + val + " Ore");
            add("block." + IndustrialReforged.MODID + ".raw_" + key + "_block", "Raw " + val + " Block");

            add("item." + IndustrialReforged.MODID + ".raw_" + key, "Raw " + val);
        }

    }

    private void addScannerInfo(String key, String val) {
        add("scanner_info." + IndustrialReforged.MODID + "." + key, val);
    }

    private void addMultiblockFeedback(String key, String val) {
        add("multiblock_feedback." + IndustrialReforged.MODID + "." + key, val);
    }

    private void addMultiblockFeedback(IRTranslations.TranslatableConstant key, String val) {
        add(key.key(), val);
    }

    private void addTitle(String key, String val) {
        add("title." + IndustrialReforged.MODID + "." + key, val);
    }

    private void addEnergyTier(Holder<EnergyTier> key, String val) {
        add("energy_tier." + IndustrialReforged.MODID + "." + key.value().name(), val);
    }

    private void addTooltip(String key, String val) {
        add("tooltip." + IndustrialReforged.MODID + "." + key, val);
    }

    private void addTooltip(IRTranslations.TranslatableConstant key, String val) {
        add(key.key(), val);
    }

    private void addCreativeTab(Supplier<? extends CreativeModeTab> tab, String val) {
        add(registryTranslation(BuiltInRegistries.CREATIVE_MODE_TAB, tab.get()).getString(), val);
    }

    private void addFluidType(Supplier<? extends FluidType> fluidType, String val) {
        add(registryTranslation(NeoForgeRegistries.FLUID_TYPES, fluidType.get()).getString(), val);
    }

    private void addFluidType(PDLFluid fluidType, String val) {
        add(registryTranslation(NeoForgeRegistries.FLUID_TYPES, fluidType.getFluidType().get()).getString(), val);
    }

    public static <T> Component registryTranslation(Registry<T> registry, T registryObject) {
        ResourceLocation objLoc = registry.getKey(registryObject);
        return Component.translatable(registry.key().location().getPath() + "." + objLoc.getNamespace() + "." + objLoc.getPath());
    }
}
