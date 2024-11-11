package com.indref.industrial_reforged.datagen.data;

import com.google.common.collect.ImmutableList;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.tags.CTags;
import com.indref.industrial_reforged.tags.IRTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class IRRecipeProvider extends RecipeProvider {
    private static final ImmutableList<ItemLike> TIN_SMELTABLES = ImmutableList.of(IRItems.RAW_TIN, IRBlocks.TIN_ORE, IRBlocks.DEEPSLATE_TIN_ORE);
    private static final ImmutableList<ItemLike> NICKEL_SMELTABLES = ImmutableList.of(IRItems.RAW_NICKEL, IRBlocks.NICKEL_ORE, IRBlocks.DEEPSLATE_NICKEL_ORE);
    private static final ImmutableList<ItemLike> LEAD_SMELTABLES = ImmutableList.of(IRItems.RAW_LEAD, IRBlocks.LEAD_ORE, IRBlocks.DEEPSLATE_LEAD_ORE);
    private static final ImmutableList<ItemLike> URANIUM_SMELTABLES = ImmutableList.of(IRItems.RAW_URANIUM, IRBlocks.URANIUM_ORE, IRBlocks.DEEPSLATE_URANIUM_ORE);
    private static final ImmutableList<ItemLike> CHROMIUM_SMELTABLES = ImmutableList.of(IRItems.RAW_CHROMIUM, IRBlocks.CHROMIUM_ORE, IRBlocks.DEEPSLATE_CHROMIUM_ORE);

    private RecipeOutput output;

    public IRRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookUpProvider) {
        super(output, lookUpProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        this.output = output;
        craftingRecipes();
        smeltingRecipes();
    }

    private void craftingRecipes() {
        rubberWoodRecipes();
        toolRecipes();
        cableRecipes();
        machineCraftingRecipes();
        multiblockRecipes();

        storageItemRecipes();

        miscRecipes();

        // Compacting recipes
        rawOreToBlockRecipes();
        ingotToBlockRecipes();
    }

    private void multiblockRecipes() {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.CERAMIC_CASTING_BASIN.get())
                .pattern("# #")
                .pattern("###")
                .define('#', IRBlocks.TERRACOTTA_BRICK.get())
                .unlockedBy("has_terracotta_brick", has(IRBlocks.TERRACOTTA_BRICK.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BLAST_FURNACE_CASTING_BASIN.get())
                .pattern("# #")
                .pattern("###")
                .define('#', IRBlocks.BLAST_FURNACE_BRICKS.get())
                .unlockedBy("has_blast_furnace_brick", has(IRBlocks.BLAST_FURNACE_BRICKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BLAST_FURNACE_FAUCET.get())
                .pattern("# #")
                .pattern(" # ")
                .define('#', IRBlocks.BLAST_FURNACE_BRICKS.get())
                .unlockedBy("has_blast_furnace_brick", has(IRBlocks.BLAST_FURNACE_BRICKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BLAST_FURNACE_HATCH.get(), 2)
                .pattern("#P#")
                .pattern("PFP")
                .pattern("#P#")
                .define('#', IRBlocks.BLAST_FURNACE_BRICKS.get())
                .define('P', CTags.Items.IRON_PLATE)
                .define('F', Blocks.BLAST_FURNACE)
                .unlockedBy("has_iron_plate", has(CTags.Items.IRON_PLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.SMALL_FIREBOX_HATCH.get(), 2)
                .pattern("#P#")
                .pattern("PFP")
                .pattern("#P#")
                .define('#', Tags.Items.INGOTS_IRON)
                .define('P', CTags.Items.IRON_PLATE)
                .define('F', Blocks.FURNACE)
                .unlockedBy("has_iron_plate", has(CTags.Items.IRON_PLATE))
                .save(output);
    }

    private void storageItemRecipes() {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.BASIC_BATTERY.get())
                .pattern(" W ")
                .pattern("#R#")
                .pattern("#R#")
                .define('#', CTags.Items.LEAD_INGOT)
                .define('W', IRItems.TIN_WIRE.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_tin_wire", has(IRItems.TIN_WIRE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.FLUID_CELL.get(), 6)
                .pattern(" # ")
                .pattern("#P#")
                .pattern(" # ")
                .define('#', CTags.Items.TIN_INGOT)
                .define('P', Tags.Items.GLASS_PANES)
                .unlockedBy("has_tin_ingot", has(CTags.Items.TIN_INGOT))
                .save(output);
    }

    private void miscRecipes() {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IRItems.CLAY_MOLD_BLANK.get(), 2)
                .requires(Items.CLAY_BALL, 3)
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.ANTENNA.get())
                .pattern("# R")
                .pattern("#S ")
                .pattern(" ##")
                .define('#', CTags.Items.STEEL_PLATE)
                .define('S', IRItems.STEEL_ROD.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_battery", has(IRItems.ADVANCED_BATTERY.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.ELECTRIC_MOTOR.get(), 2)
                .pattern(" SR")
                .pattern("C#S")
                .pattern("#C ")
                .define('#', IRItems.COPPER_WIRE.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('R', IRItems.STEEL_ROD.get())
                .define('S', CTags.Items.STEEL_INGOT)
                .unlockedBy("has_copper_wire", has(IRItems.COPPER_WIRE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.CIRCUIT_BOARD.get(), 2)
                .pattern("###")
                .pattern("RRR")
                .pattern("###")
                .define('#', IRItems.PLANT_BALL)
                .define('R', CTags.Items.RUBBER_SHEET)
                .unlockedBy("has_rubber_sheet", has(CTags.Items.RUBBER_SHEET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IRItems.BASIC_CIRCUIT.get())
                .pattern("###")
                .pattern("RBR")
                .pattern("###")
                .define('#', IRBlocks.COPPER_CABLE)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('B', IRItems.CIRCUIT_BOARD)
                .unlockedBy("has_copper_cable", has(IRBlocks.COPPER_CABLE))
                .save(output);
    }

    private void smeltingRecipes() {
        ingotSmeltingRecipes();
    }

    private void toolRecipes() {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.HAMMER.get())
                .pattern(" # ")
                .pattern(" S#")
                .pattern("S  ")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('#', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.WRENCH.get())
                .pattern(" # ")
                .pattern(" ##")
                .pattern("#  ")
                .define('#', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.TREE_TAP.get())
                .pattern(" S ")
                .pattern("###")
                .pattern("#  ")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('#', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.THERMOMETER.get())
                .pattern(" ##")
                .pattern("#R#")
                .pattern("R# ")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('#', Tags.Items.GLASS_PANES)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.TOOLBOX.get())
                .pattern(" R ")
                .pattern("# #")
                .pattern("###")
                .define('R', IRItems.IRON_ROD)
                .define('#', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, IRItems.BLUEPRINT.get())
                .requires(Tags.Items.DYES_BLUE)
                .requires(Items.PAPER)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.ELECTRIC_DRILL.get())
                .pattern(" ##")
                .pattern("CM#")
                .pattern("BC ")
                .define('#', CTags.Items.STEEL_INGOT)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('M', IRItems.ELECTRIC_MOTOR.get())
                .define('B', IRItems.BASIC_BATTERY.get())
                .unlockedBy("has_motor", has(IRItems.ELECTRIC_MOTOR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.ADVANCED_DRILL.get())
                .pattern(" ##")
                .pattern("CD#")
                .pattern("BC ")
                .define('#', Tags.Items.GEMS_DIAMOND)
                .define('D', IRItems.ELECTRIC_DRILL.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', IRItems.ADVANCED_BATTERY.get())
                .unlockedBy("has_battery", has(IRItems.ADVANCED_BATTERY.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.ROCK_CUTTER.get())
                .pattern("D# ")
                .pattern(" D#")
                .pattern(" CB")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('#', CTags.Items.STEEL_INGOT)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', IRItems.BASIC_BATTERY.get())
                .unlockedBy("has_battery", has(IRItems.ADVANCED_BATTERY.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, IRItems.SCANNER.get())
                .pattern("A  ")
                .pattern("#G#")
                .pattern("#C#")
                .define('A', IRItems.ANTENNA)
                .define('G', Tags.Items.GLASS_PANES)
                .define('C', IRItems.BASIC_CIRCUIT)
                .define('#', CTags.Items.STEEL_PLATE)
                .unlockedBy("has_circuit", has(IRItems.BASIC_CIRCUIT.get()))
                .save(output);
    }

    private void machineCraftingRecipes() {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BASIC_MACHINE_FRAME.get(), 2)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .define('#', CTags.Items.ALUMINUM_INGOT)
                .unlockedBy("has_aluminum_ingot", has(CTags.Items.ALUMINUM_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.BASIC_GENERATOR.get())
                .pattern("B")
                .pattern("M")
                .pattern("F")
                .define('B', IRItems.BASIC_BATTERY.get())
                .define('M', IRBlocks.BASIC_MACHINE_FRAME.get())
                .define('F', Blocks.FURNACE)
                .unlockedBy("has_machine_frame", has(IRBlocks.BASIC_MACHINE_FRAME.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.CENTRIFUGE.get())
                .pattern("###")
                .pattern("RBR")
                .pattern("#M#")
                .define('#', CTags.Items.IRON_PLATE)
                .define('R', IRItems.IRON_ROD)
                .define('B', IRBlocks.BASIC_MACHINE_FRAME)
                .define('M', IRItems.ELECTRIC_MOTOR)
                .unlockedBy("has_machine_frame", has(IRBlocks.BASIC_MACHINE_FRAME.get()))
                .save(output);

        // "MANUAL" Machines

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.CRAFTING_STATION.get())
                .pattern("WWW")
                .pattern("#R#")
                .pattern("#C#")
                .define('W', ItemTags.WOOL)
                .define('#', ItemTags.PLANKS)
                .define('R', Blocks.CRAFTING_TABLE)
                .define('C', Blocks.CHEST)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.DRAIN.get())
                .pattern("PBP")
                .pattern("#U#")
                .pattern("###")
                .define('P', ItemTags.PLANKS)
                .define('#', Tags.Items.COBBLESTONES)
                .define('U', Items.BUCKET)
                .define('B', Blocks.IRON_BARS)
                .unlockedBy("has_cobblestone", has(Tags.Items.COBBLESTONES))
                .save(output);

        // MISC

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IRBlocks.COIL.get())
                .pattern("###")
                .pattern("#R#")
                .pattern("###")
                .define('#', IRItems.COPPER_WIRE.get())
                .define('R', IRItems.IRON_ROD)
                .unlockedBy("has_copper_wire", has(IRItems.COPPER_WIRE.get()))
                .save(output);
    }

    private void rawOreToBlockRecipes() {
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_BAUXITE.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_BAUXITE_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_CHROMIUM.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_CHROMIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_IRIDIUM.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_IRIDIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_LEAD.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_LEAD_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_NICKEL.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_NICKEL_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_TIN.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_TIN_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_URANIUM.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_URANIUM_BLOCK.get());
    }

    private void ingotToBlockRecipes() {
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.STEEL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.STEEL_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.ALUMINUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.ALUMINUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.TITANIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.TITANIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.CHROMIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.CHROMIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.IRIDIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.IRIDIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.LEAD_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.LEAD_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.NICKEL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.NICKEL_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.TIN_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.TIN_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.URANIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.URANIUM_BLOCK.get());
    }

    private void cableRecipes() {
        cable(CTags.Items.TIN_WIRE, IRBlocks.TIN_CABLE.get());
        cable(CTags.Items.COPPER_WIRE, IRBlocks.COPPER_CABLE.get());
        cable(CTags.Items.GOLD_WIRE, IRBlocks.GOLD_CABLE.get());
        cable(CTags.Items.STEEL_WIRE, IRBlocks.STEEL_CABLE.get());
    }

    private void rubberWoodRecipes() {
        planksFromLog(output, IRBlocks.RUBBER_TREE_PLANKS, IRTags.Items.RUBBER_LOGS, 4);
        slab(this.output, RecipeCategory.BUILDING_BLOCKS, IRBlocks.RUBBER_TREE_SLAB, IRBlocks.RUBBER_TREE_PLANKS);
        pressurePlate(output, IRBlocks.RUBBER_TREE_PRESSURE_PLATE, IRBlocks.RUBBER_TREE_PLANKS);
        woodFromLogs(output, IRBlocks.RUBBER_TREE_WOOD, IRBlocks.RUBBER_TREE_LOG);
        woodFromLogs(output, IRBlocks.STRIPPED_RUBBER_TREE_WOOD, IRBlocks.STRIPPED_RUBBER_TREE_WOOD);
        stairBuilder(IRBlocks.RUBBER_TREE_STAIRS, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        buttonBuilder(IRBlocks.RUBBER_TREE_BUTTON, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        fenceBuilder(IRBlocks.RUBBER_TREE_FENCE, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        fenceGateBuilder(IRBlocks.RUBBER_TREE_FENCE_GATE, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        trapdoorBuilder(IRBlocks.RUBBER_TREE_TRAPDOOR, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
        doorBuilder(IRBlocks.RUBBER_TREE_DOOR, Ingredient.of(IRBlocks.RUBBER_TREE_PLANKS))
                .unlockedBy("has_planks", has(IRBlocks.RUBBER_TREE_PLANKS))
                .save(output);
    }

    private void ingotSmeltingRecipes() {
        oreSmelting(output, TIN_SMELTABLES, RecipeCategory.MISC, IRItems.TIN_INGOT, 0.7f, 200, "tin_ingot");
        oreSmelting(output, NICKEL_SMELTABLES, RecipeCategory.MISC, IRItems.NICKEL_INGOT, 0.7f, 200, "nickel_ingot");
        oreSmelting(output, LEAD_SMELTABLES, RecipeCategory.MISC, IRItems.LEAD_INGOT, 0.7f, 200, "lead_ingot");
        oreSmelting(output, CHROMIUM_SMELTABLES, RecipeCategory.MISC, IRItems.CHROMIUM_INGOT, 1f, 200, "chromium_ingot");
        oreSmelting(output, URANIUM_SMELTABLES, RecipeCategory.MISC, IRItems.URANIUM_INGOT, 1f, 200, "uranium_ingot");

        oreBlasting(output, TIN_SMELTABLES, RecipeCategory.MISC, IRItems.TIN_INGOT, 0.7f, 100, "tin_ingot");
        oreBlasting(output, NICKEL_SMELTABLES, RecipeCategory.MISC, IRItems.NICKEL_INGOT, 0.7f, 100, "nickel_ingot");
        oreBlasting(output, LEAD_SMELTABLES, RecipeCategory.MISC, IRItems.LEAD_INGOT, 0.7f, 100, "lead_ingot");
        oreBlasting(output, CHROMIUM_SMELTABLES, RecipeCategory.MISC, IRItems.CHROMIUM_INGOT, 1f, 100, "chromium_ingot");
        oreBlasting(output, URANIUM_SMELTABLES, RecipeCategory.MISC, IRItems.URANIUM_INGOT, 1f, 100, "uranium_ingot");
    }

    // -- HELPER METHODS --
    private void cable(TagKey<Item> wireMaterial, ItemLike cable) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, cable)
                .requires(CTags.Items.RUBBER_SHEET)
                .requires(wireMaterial)
                .unlockedBy("has_rubber_sheet", has(CTags.Items.RUBBER_SHEET))
                .save(output);
    }

    protected static void nineBlockStorageRecipes(
            RecipeOutput recipeOutput, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed
    ) {
        nineBlockStorageRecipes(
                recipeOutput, unpackedCategory, unpacked, packedCategory, packed, getSimpleRecipeName(packed), null, getSimpleRecipeName(unpacked), null
        );
    }

    protected static void nineBlockStorageRecipes(
            RecipeOutput recipeOutput,
            RecipeCategory unpackedCategory,
            ItemLike unpacked,
            RecipeCategory packedCategory,
            ItemLike packed,
            String packedName,
            @Nullable String packedGroup,
            String unpackedName,
            @Nullable String unpackedGroup
    ) {
        ShapelessRecipeBuilder.shapeless(unpackedCategory, unpacked, 9)
                .requires(packed)
                .group(unpackedGroup)
                .unlockedBy(getHasName(packed), has(packed))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, unpackedName));
        ShapedRecipeBuilder.shaped(packedCategory, packed)
                .define('#', unpacked)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group(packedGroup)
                .unlockedBy(getHasName(unpacked), has(unpacked))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, packedName));
    }
}
