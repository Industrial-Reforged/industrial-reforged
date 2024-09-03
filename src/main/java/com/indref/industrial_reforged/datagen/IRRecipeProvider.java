package com.indref.industrial_reforged.datagen;

import com.google.common.collect.ImmutableList;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.tags.CTags;
import com.indref.industrial_reforged.tags.IRTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

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

        // Compacting recipes
        rawOreToBlockRecipes();
        ingotToBlockRecipes();
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
        // TODO: Other tool recipes
    }

    private void rawOreToBlockRecipes() {
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.RAW_BAUXITE.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.RAW_BAUXITE_BLOCK.get());
        // TODO: Other raw blocks
    }

    private void ingotToBlockRecipes() {
        nineBlockStorageRecipes(output, RecipeCategory.MISC, IRItems.STEEL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, IRBlocks.STEEL_BLOCK.get());
        // TODO: Other metal blocks
    }

    private void cableRecipes() {
        cable(CTags.Items.TIN_INGOT, CTags.Items.TIN_WIRE, IRBlocks.TIN_CABLE.get());
        // TODO: Other cables
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
        doorBuilder(IRBlocks.RUBBER_TREE_DOOR, Ingredient.of(IRBlocks.RUBBER_TREE_DOOR))
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
    private void cable(TagKey<Item> cableMaterial, TagKey<Item> wireMaterial, ItemLike cable) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, cable, 6)
                .pattern("###")
                .pattern("WWW")
                .pattern("###")
                .define('#', CTags.Items.RUBBER_SHEET)
                .define('W', cableMaterial)
                .unlockedBy("has_rubber_sheet", has(CTags.Items.RUBBER_SHEET))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, cable)
                .requires(CTags.Items.RUBBER_SHEET)
                .requires(wireMaterial)
                .unlockedBy("has_rubber_sheet", has(CTags.Items.RUBBER_SHEET))
                .save(output);
    }
}
